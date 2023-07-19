package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.util.Utils;
import rs.etf.pp1.symboltable.concepts.*;

public class SemanticPass extends VisitorAdaptor {
	Obj currentMethod = null;
	boolean returnFound = false;
	boolean errorDetected = false;
	
	int nVars;
	
	Logger log = Logger.getLogger(getClass());
	
	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji " + line);
		log.error(msg.toString());
	}
	
	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji " + line);
		log.info(msg.toString());
	}
	
	public void visit(PrintStatement print) {
		if (print.getFactor().struct != Utils.intStruct && print.getFactor().struct != Utils.charStruct && print.getFactor().struct != Utils.boolStruct)
			report_error("Semantic error: PRINT operand must be int, char or bool", print);
	}

	public void visit(NumConst cnst) {
		cnst.struct = Utils.intStruct;
	}
	
	public void visit(CharConst cnst) {
		cnst.struct = Utils.charStruct;
	}
	
	public void visit(BoolConst cnst) {
		cnst.struct = Utils.boolStruct;
	}
	
	public boolean passed() {
		return !errorDetected;
	}
}
