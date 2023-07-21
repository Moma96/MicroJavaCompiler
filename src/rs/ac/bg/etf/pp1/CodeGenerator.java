package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.BoolConst;
import rs.ac.bg.etf.pp1.ast.CharConst;
import rs.ac.bg.etf.pp1.ast.MethodDecl;
import rs.ac.bg.etf.pp1.ast.MethodTypeName;
import rs.ac.bg.etf.pp1.ast.NumConst;
import rs.ac.bg.etf.pp1.ast.PrintStatement;
import rs.ac.bg.etf.pp1.ast.ProgramName;
import rs.ac.bg.etf.pp1.ast.SyntaxNode;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.ac.bg.etf.pp1.util.Utils;
import rs.etf.pp1.mj.runtime.Code;

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
		Utils.Initialize();
	}
	
	public void visit(PrintStatement print) {
		if (Utils.intStruct == print.getFactor().struct) {
			Code.loadConst(5);
			Code.put(Code.print);
		} else if (Utils.charStruct == print.getFactor().struct) {
			Code.loadConst(5);
			Code.put(Code.bprint);
		} else if (Utils.boolStruct == print.getFactor().struct) {
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
		
		Code.put(Code.enter);
		Code.put(0);
		Code.put(0);
	}
	
	public void visit(MethodDecl methodDecl) {
		// code for function exit
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
}
