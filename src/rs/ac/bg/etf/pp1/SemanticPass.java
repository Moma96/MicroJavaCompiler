package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.util.Utils;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class SemanticPass extends VisitorAdaptor {
	private boolean errorDetected = false;
	
	private Obj currentMethod = null;
	private List<ConstAssignment> currentConstAssignments = new ArrayList<ConstAssignment>();
	private List<VarDeclName> currentVarDeclNames = new ArrayList<VarDeclName>();
	
	public int nProgramVars;
	
	Logger log = Logger.getLogger(getClass());
	
	private void report_operations_type_error(SyntaxNode info) {
		report_error("Math operations are supported only for Int types", info);
	}
	
	private void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder("Semantic error: " + message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" in line " + line);
		log.error(msg.toString());
	}
	
	private void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" in line " + line);
		log.info(msg.toString());
	}
	
	public void visit(ProgramName programName) {
		programName.obj = Tab.insert(Obj.Prog, programName.getName(), Tab.noType);
		Tab.openScope();
		log.info("Program openScope");
	}
	
	public void visit(Program program) {
		nProgramVars = Tab.currentScope.getnVars();
		Tab.chainLocalSymbols(program.getProgramName().obj);
		Tab.closeScope();
		log.info("Program closeScope");
	}
	
	public void visit(MethodTypeName methodTypeName) {
		currentMethod = Tab.insert(Obj.Meth, methodTypeName.getMethName(), Tab.noType); // Only void functions are currently supported
		methodTypeName.obj = currentMethod;
		Tab.openScope();
		report_info("function '" + methodTypeName.getMethName() + "' processing started", methodTypeName);
	}
	
	public void visit(MethodDecl methodDecl) {
		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();
		report_info("function '" + currentMethod.getName() + "' processing is finished", methodDecl);
		
		currentMethod = null;
	}
		
	public void visit(ConstDecl constDecl) {
		for(ConstAssignment constAssignment: currentConstAssignments) {
			if (Tab.find(constAssignment.getConstName()) != Tab.noObj) {
				report_error("Const '" + constAssignment.getConstName() + "' is already declared in current scope", constDecl);
				constAssignment.obj = Tab.noObj;
			} else if (!constAssignment.getConst().struct.assignableTo(constDecl.getType().struct)){
				report_error("Const '" + constAssignment.getConstName() + "' type is not compatible with assigned value", constDecl);
				constAssignment.obj = Tab.noObj;
			} else {
				report_info("Const declared '" + constAssignment.getConstName() + "'", constDecl);
				int constValue = Utils.getConstValue(constAssignment.getConst());
				constAssignment.obj = Utils.createGlobalNamedConst(constValue, constAssignment.getConstName(), constDecl.getType().struct);
			}
		}
		currentConstAssignments.clear();
	}
	
	public void visit(ConstAssignment constAssignment) {
		currentConstAssignments.add(constAssignment);
	}
	
	public void visit(VarDecl varDecl) {
		for(VarDeclName varDeclName: currentVarDeclNames) {
			if (Tab.find(varDeclName.getVarName()) != Tab.noObj) {
				report_error("Var '" + varDeclName.getVarName() + "' is already declared in current scope", varDecl);
			} else {
				if (varDeclName.getVarDeclKind() instanceof SimpleVarDeclKind) {
					Tab.insert(Obj.Var, varDeclName.getVarName(), varDecl.getType().struct);
					report_info("Var declared '" + varDeclName.getVarName() + "'", varDecl);
				} else if (varDeclName.getVarDeclKind() instanceof ArrayVarDeclKind) {
					Struct elemType = varDecl.getType().struct;
					Struct arrayType = Utils.arrayType(elemType);
					Tab.insert(Obj.Var, varDeclName.getVarName(), arrayType);
					report_info("Array declared '" + varDeclName.getVarName() + "'", varDecl);
				} else {
					report_error("Unrecognized var declaration kind", varDecl);
				}
			}
		}
		currentVarDeclNames.clear();
	}
	
	public void visit(VarDeclName varDeclName) {
		currentVarDeclNames.add(varDeclName);
	}
	
	public void visit(Designator designator) {
		Obj obj = Tab.find(designator.getName());
		if (obj == Tab.noObj) {
			report_error("Name " + designator.getName() + " is not declared", designator);
			designator.obj = Tab.noObj;
			return;
		}
		if (designator.getDesignatorKind() instanceof ElemDesignatorKind) {
			ElemDesignatorKind elemDesignatorKind = (ElemDesignatorKind)designator.getDesignatorKind();
			if (!elemDesignatorKind.getExpr().struct.equals(Tab.intType)) {
				report_error("Array index must be Int type", designator);
				designator.obj = Tab.noObj;
				return;
			} else {
				designator.obj = new Obj(Obj.Elem, obj.getName(), obj.getType().getElemType(), obj.getAdr(), obj.getLevel());
			}
		} else {
			designator.obj = obj;
		}
	}
	
	public void visit(ElemDesignKindFirstNode firstNode) {
		// Save array obj
		Designator designator = (Designator)firstNode.getParent().getParent();
		firstNode.obj = Tab.find(designator.getName());
	}

	
	public void visit(Assignment assignment) {
		Struct rhsType = assignment.getRightHandSide().struct;
		Struct lhsType = assignment.getDesignator().obj.getType();
		if (!rhsType.assignableTo(lhsType)) {
			report_error("Not compatible types in assignment", assignment);
		}
		int destinationKind = assignment.getDesignator().obj.getKind() ;
		if (destinationKind != Obj.Var && destinationKind != Obj.Elem) {
			report_error("Destination is not a variable", assignment);
		}
	}
	
	public void visit(RightHandFindAny rhFindAny) {
		rhFindAny.struct = Utils.boolType;
		Struct arrayType = rhFindAny.getDesignator().obj.getType();
		report_info(rhFindAny.toString(), rhFindAny);
		if (arrayType.getKind() != Struct.Array) {
			report_error("FindAny is supported only for arrays", rhFindAny);
			return;
		}
		Struct exprType = rhFindAny.getExpr().struct;
		if (!exprType.assignableTo(arrayType.getElemType())) {
			report_error("Array element type is uncompatible with expression type", rhFindAny);
			return;
		}
	}
	
	public void visit(RightHandExpression rhExpr) {
		rhExpr.struct = rhExpr.getExpr().struct;
	}
	
	public void visit(IncStatement inc) {
		Struct designatorType = inc.getDesignator().obj.getType();
		if (!designatorType.equals(Tab.intType)) {
			report_operations_type_error(inc);
		}
		int destinationKind = inc.getDesignator().obj.getKind() ;
		if (destinationKind != Obj.Var && destinationKind != Obj.Elem) {
			report_error("Increment operand is not a variable", inc);
		}
	}
	
	public void visit(DecStatement dec) {
		Struct designatorType = dec.getDesignator().obj.getType();
		if (!designatorType.equals(Tab.intType)) {
			report_operations_type_error(dec);
		}
		int destinationKind = dec.getDesignator().obj.getKind() ;
		if (destinationKind != Obj.Var && destinationKind != Obj.Elem) {
			report_error("Decrement operand is not a variable", dec);
		}
	}
	
	public void visit(Type type) {
		Obj typeNode = Tab.find(type.getTypeName());
		if (typeNode == Tab.noObj) {
			report_error("Type " + type.getTypeName() + " not found in symbol table", type);
			type.struct = Tab.noType;
		} else if (typeNode.getKind() != Obj.Type) {
			report_error("Identifier " + type.getTypeName() + " is not a type", type);
			type.struct = Tab.noType;
		} else {
			type.struct = typeNode.getType();
		}
	}
	
	public void visit(ReadStatement read) {
		Struct designatorType = read.getDesignator().obj.getType();
		if (!Utils.isIntOrChar(designatorType))
			report_error("READ operand must be int or char", read);
	}
	
	public void visit(PrintStatement print) {
		Struct factorStruct = print.getExpr().struct;
		if (!Utils.isSympleType(factorStruct))
			report_error("PRINT operand must be int, char or bool", print);
	}

	public void visit(AddopExpr addopExpr) {
		Struct exprType = addopExpr.getExpr().struct;
		Struct termType = addopExpr.getTerm().struct;
		if (!exprType.equals(termType) || !exprType.equals(Tab.intType)) {
			report_operations_type_error(addopExpr);
			addopExpr.struct = Tab.noType;
		} else {
			addopExpr.struct = exprType;
		}
	}
	
	public void visit(ExprNegTerm exprNegTerm) {
		Struct termType = exprNegTerm.getTerm().struct;
		if (!termType.equals(Tab.intType)) {
			report_operations_type_error(exprNegTerm);
			exprNegTerm.struct = Tab.noType;
		} else {			
			exprNegTerm.struct = termType;
		}
	}
	
	public void visit(ExprTerm exprTerm) {
		exprTerm.struct = exprTerm.getTerm().struct;
	}
	
	public void visit(MulopTerm mulopTerm) {
		Struct termType = mulopTerm.getTerm().struct;
		Struct factorType = mulopTerm.getFactor().struct;
		if (!termType.equals(factorType) || !termType.equals(Tab.intType)) {
			report_operations_type_error(mulopTerm);
			mulopTerm.struct = Tab.noType;
		} else {
			mulopTerm.struct = termType;
		}
	}
	
	public void visit(TermFactor termFactor) {
		termFactor.struct = termFactor.getFactor().struct;
	}
	
	public void visit(DesignatorFactor designatorFactor) {
		designatorFactor.struct = designatorFactor.getDesignator().obj.getType();
	}
	
	public void visit(ConstFactor constFactor) {
		constFactor.struct = constFactor.getConst().struct;
	}
	
	public void visit(ParenFactor parenFactor) {
		parenFactor.struct = parenFactor.getExpr().struct;
	}
	
	public void visit(ArrayFactor arrayFactor) {
		if (!arrayFactor.getExpr().struct.equals(Tab.intType)) {
			report_error("Array length must be Int type", arrayFactor);
			arrayFactor.struct = Tab.noType;
		}
		arrayFactor.struct = Utils.arrayType(arrayFactor.getType().struct);
	}
	
	public void visit(NumConst cnst) {
		cnst.struct = Tab.intType;
	}
	
	public void visit(CharConst cnst) {
		cnst.struct = Tab.charType;
	}
	
	public void visit(BoolConst cnst) {
		cnst.struct = Utils.boolType;
	}
	
	public boolean passed() {
		return !errorDetected;
	}
}
