package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.CounterVisitor.VarCounter;
import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.util.Utils;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

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
		
		if (DesignatorFactor.class == parent.getClass() || RightHandFindAny.class == parent.getClass()) {
			// Designator is right-hand side operand
			Code.load(designator.obj);
		}
	}
	
	public void visit(ElemDesignKindFirstNode firstNode) {
		// Put array address before index
		Code.load(firstNode.obj);
	}
	
	public void visit(Assignment assignment) {
		Struct sourceType = assignment.getRightHandSide().struct;
		if (sourceType.getKind() == Struct.Array) {
			Code.put(Code.newarray);
			if (sourceType.getElemType() == Tab.charType) {
				Code.put(0);
			} else {
				Code.put(1);
			}
		}
		Code.store(assignment.getDesignator().obj);
	}
	
	public void visit(IncStatement inc) {
		Obj designatorObj = inc.getDesignator().obj;
		if (inc.getDesignator().getDesignatorKind() instanceof ElemDesignatorKind) {
			// Double adr and index so load and store have enough to consume
			Code.put(Code.dup2);
		}
		Code.load(designatorObj);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(inc.getDesignator().obj);
	}
	
	public void visit(DecStatement dec) {
		Obj designatorObj = dec.getDesignator().obj;
		if (dec.getDesignator().getDesignatorKind() instanceof ElemDesignatorKind) {
			// Double adr and index so load and store have enough to consume
			Code.put(Code.dup2);
		}
		Code.load(designatorObj);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.store(dec.getDesignator().obj);
	}
	
	//region PRINT
	
	static int DEFAULT_PRINT_WIDTH = 5;
	
	public void visit(PrintStatement print) {
		// first argument is loaded - value
		// second argument is loaded - width
		if (Tab.intType.equals(print.getExpr().struct)) {
			Code.put(Code.print);
		} else if (Tab.charType.equals(print.getExpr().struct)) {
			Code.put(Code.bprint);
		} else if (Utils.boolType.equals(print.getExpr().struct)) {
			int printBoolAdrOffset = Utils.getPrintBoolAdr() - Code.pc;
			Code.put(Code.call);
			Code.put2(printBoolAdrOffset);
		}
	}
	
	public void visit(PrintWidth printWidth) {
		Code.loadConst(printWidth.getNum());
	}
	
	public void visit(NoPrintWidth noPrintWidth) {
		Code.loadConst(DEFAULT_PRINT_WIDTH);
	}
	
	//endregion PRINT
	
	//region Mathematical operations
	
	public void visit(AddopExpr addopExpr) {
		Addop addop = addopExpr.getAddop();
		if (addop instanceof Plus) {
			Code.put(Code.add);
		} else if (addop instanceof Minus) {
			Code.put(Code.sub);
		}
	}
	
	public void visit(ExprNegTerm exprNegTerm) {
		Code.put(Code.neg);
	}
	
	public void visit(MulopTerm mulopTerm) {
		Mulop mulop = mulopTerm.getMulop();
		if (mulop instanceof Mul) {
			Code.put(Code.mul);
		} else if (mulop instanceof Div) {
			Code.put(Code.div);
		} else if (mulop instanceof Mod) {
			Code.put(Code.rem);
		}
	}
	
	//endregion Mathematical operations
	
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
	
	public void visit(RightHandFindAny findAny) {
		// first argument is loaded - array address
		// second argument is loaded - expression result
		int findAnyAdrOffset = Utils.getFindAnyAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(findAnyAdrOffset);
	}
}
