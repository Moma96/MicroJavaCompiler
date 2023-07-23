package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.CounterVisitor.VarCounter;
import rs.ac.bg.etf.pp1.ast.Assignment;
import rs.ac.bg.etf.pp1.ast.BoolConst;
import rs.ac.bg.etf.pp1.ast.CharConst;
import rs.ac.bg.etf.pp1.ast.Designator;
import rs.ac.bg.etf.pp1.ast.MethodDecl;
import rs.ac.bg.etf.pp1.ast.MethodTypeName;
import rs.ac.bg.etf.pp1.ast.NumConst;
import rs.ac.bg.etf.pp1.ast.PrintStatement;
import rs.ac.bg.etf.pp1.ast.ProgramName;
import rs.ac.bg.etf.pp1.ast.SyntaxNode;
import rs.ac.bg.etf.pp1.ast.Var;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.ac.bg.etf.pp1.util.Utils;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;

public class CodeGenerator extends VisitorAdaptor {
	
	private int mainPc;
	
	Logger log = Logger.getLogger(getClass());
	
	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" in line " + line);
		log.info(msg.toString());
	}
	
	public int getMainPc() {
		return mainPc;
	}
	
	public void visit(ProgramName programName) {
		Utils.codeGeneratorInit();
	}
	
	public void visit(Designator designator) {
		SyntaxNode parent = designator.getParent();

		if (Var.class == parent.getClass()) {
			Code.load(designator.obj);
		}
	}
	
	public void visit(Assignment assignment) {
		Code.store(assignment.getDesignator().obj);
	}
	
	public void visit(PrintStatement print) {
		int defaultWidth = 5;
		// first argument is already on estack - value
		// load second argument - width
		Code.loadConst(defaultWidth);
		if (Tab.intType.equals(print.getFactor().struct)) {
			Code.put(Code.print);
		} else if (Tab.charType.equals(print.getFactor().struct)) {
			Code.put(Code.bprint);
		} else if (Utils.boolType.equals(print.getFactor().struct)) {
			int printBoolAdrOffset = Utils.getPrintBoolAdr() - Code.pc;
			Code.put(Code.call);
			Code.put2(printBoolAdrOffset);
		}
	}
	
	public void visit(NumConst cnst) {
		Code.load(Utils.createGlobalConst(cnst.getN1(), cnst.struct));
	}
	
	public void visit(CharConst cnst) {
		Code.load(Utils.createGlobalConst(cnst.getC1(), cnst.struct));
	}
	
	public void visit(BoolConst cnst) {
		Code.load(Utils.createGlobalConst(cnst.getB1(), cnst.struct));
	}
	
	public void visit(MethodTypeName methodTypeName) {
		if ("main".equalsIgnoreCase(methodTypeName.getMethName())) {
			mainPc = Code.pc;
		}
		
		methodTypeName.obj.setAdr(Code.pc);
		SyntaxNode methodNode = methodTypeName.getParent();
		
		VarCounter varCnt = new VarCounter();
		methodNode.traverseTopDown(varCnt);
				
		Code.put(Code.enter);
		// method parameters are not currently supported
		Code.put(0);
		Code.put(0 + varCnt.getCount());
	}
	
	public void visit(MethodDecl methodDecl) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
}
