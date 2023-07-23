package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.util.Utils;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.*;

public class SemanticPass extends VisitorAdaptor {
	private boolean errorDetected = false;
	
	private Obj currentMethod = null;
	private List<ConstAssignment> currentConstAssignments = new ArrayList<ConstAssignment>();
	private List<VarDeclName> currentVarDeclNames = new ArrayList<VarDeclName>();
	
	public int nProgramVars;
	
	Logger log = Logger.getLogger(getClass());
	
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
			} else if (!constAssignment.getConst().struct.assignableTo(constDecl.getType().struct)){
				report_error("Const '" + constAssignment.getConstName() + "' type is not compatible with assigned value", constDecl);
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
				report_info("Var declared '" + varDeclName.getVarName() + "'", varDecl);
				Tab.insert(Obj.Var, varDeclName.getVarName(), varDecl.getType().struct);	
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
		}
		designator.obj = obj;
	}
	
	public void visit(Assignment assignment) {
		Struct sourceType = assignment.getTerm().struct;
		Struct destinationType = assignment.getDesignator().obj.getType();
		if (!sourceType.assignableTo(destinationType)) {
			report_error("Not compatible types in assignment", assignment);
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
	
	public void visit(PrintStatement print) {
		Struct factorStruct = print.getTerm().struct;
		if (!Utils.isSympleType(factorStruct))
			report_error("PRINT operand must be int, char or bool", print);
	}

	public void visit(MulopTerm mulopTerm) {
		Struct termType = mulopTerm.getTerm().struct;
		Struct factorType = mulopTerm.getFactor().struct;
		if (!termType.equals(factorType) || !termType.equals(Tab.intType)) {
			report_error("Math operations are supported only for Int types", mulopTerm);
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
