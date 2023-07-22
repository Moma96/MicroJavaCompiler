package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.util.Utils;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.*;

public class SemanticPass extends VisitorAdaptor {
	Obj currentMethod = null;
	boolean returnFound = false;
	boolean errorDetected = false;
	
	int nVars;
	
	Logger log = Logger.getLogger(getClass());
	
	private void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
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
		nVars = Tab.currentScope.getnVars();
		Tab.chainLocalSymbols(program.getProgramName().obj);
		Tab.closeScope();
		log.info("Program closeScope");
	}
	
	public void visit(MethodTypeName methodTypeName) {
		currentMethod = Tab.insert(Obj.Meth, methodTypeName.getMethName(), Tab.noType); // Struct should be return type of the method
		methodTypeName.obj = currentMethod;
		Tab.openScope();
		report_info("function '" + methodTypeName.getMethName() + "' processing started", methodTypeName);
	}
	
	public void visit(MethodDecl methodDecl) {
		if (!returnFound && currentMethod.getType() != Tab.noType) {
			report_error("Greska: funkcija " + currentMethod.getName() + " nema return iskaz", methodDecl);
		}
		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();
		report_info("function '" + currentMethod.getName() + "' processing is finished", methodDecl);
		
		returnFound = false;
		currentMethod = null;
	}
	
	public void visit(VarDecl varDecl) {
		report_info("Var declared '" + varDecl.getVarName() + "'", varDecl);
		Tab.insert(Obj.Var, varDecl.getVarName(), varDecl.getType().struct);
	}
	
	public void visit(Designator designator) {
		Obj obj = Tab.find(designator.getName());
		if (obj == Tab.noObj) {
			report_error("Error: name " + designator.getName() + " is not declared", designator);
		}
		designator.obj = obj;
	}
	
	public void visit(Type type) {
		Obj typeNode = Tab.find(type.getTypeName());
		if (typeNode == Tab.noObj) {
			report_error("Type " + type.getTypeName() + " not found in symbol table", type);
			type.struct = Tab.noType;
		} else if (typeNode.getKind() != Obj.Type) {
			report_error("Error: Identifier " + type.getTypeName() + " is not a type", type);
			type.struct = Tab.noType;
		} else {
			type.struct = typeNode.getType();
		}
	}
	
	public void visit(PrintStatement print) {
		Struct factorStruct = print.getFactor().struct;
		if (!factorStruct.equals(Tab.intType) && !factorStruct.equals(Tab.charType) && !factorStruct.equals(Utils.boolType))
			report_error("Semantic error: PRINT operand must be int, char or bool", print);
	}

	public void visit(Var var) {
		var.struct = var.getDesignator().obj.getType();
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
