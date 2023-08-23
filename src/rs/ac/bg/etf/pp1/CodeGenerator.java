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
	
	public void visit(Assignment assignment) {
		Struct sourceType = assignment.getRightHandSide().struct;
		if (sourceType.getKind() == Struct.Array) {
			// length is loaded
			// duplicate n to spend one for array length check
			Code.put(Code.dup);
			Utils.callMethod(Utils.getArrayLengthGuardAdr());

			Code.put(Code.newarray);
			if (sourceType.getElemType() == Tab.charType) {
				Code.put(0);
			} else {
				Code.put(1);
			}
		}
		Code.store(assignment.getDesignator().obj);
	}
	
	//region Arrays
	
	public void visit(ElemDesignKindFirstNode firstNode) {
		// Put array address before index
		Code.load(firstNode.obj);
	}
	
	public void visit(ElemDesignatorKind elemDesignatorKind) {
		// duplicate array address and index and then pop index for member access check
		Code.put(Code.dup2);
		Code.put(Code.pop);
		Utils.callMethod(Utils.getArrayMemberAccessGuardAdr());
		
		// duplicate array address and index for array index check
		Code.put(Code.dup2);
		Utils.callMethod(Utils.getArrayIndexGuardAdr());
	}
	
	public void visit(RightHandFindAny findAny) {
		// first argument is loaded - array address
		// second argument is loaded - expression result
		Utils.callMethod(Utils.getFindAnyAdr());
	}
	
	//endregion Arrays
	
	public void visit(ReadStatement read) {
		Obj designator = read.getDesignator().obj;
		Struct designatorType = designator.getType();
		if (Tab.charType.equals(designatorType)) {
			Code.put(Code.bread);
		} else {
			Code.put(Code.read);
		}
		Code.store(designator);
	}
	
	//region Print
	
	static int DEFAULT_PRINT_WIDTH = 5;
	
	public void visit(PrintStatement print) {
		// first argument is loaded - value
		// second argument is loaded - width
		Struct exprType = print.getExpr().struct;
		if (Tab.intType.equals(exprType)) {
			Code.put(Code.print);
		} else if (Tab.charType.equals(exprType)) {
			Code.put(Code.bprint);
		} else if (Utils.boolType.equals(exprType)) {
			Utils.callMethod(Utils.getPrintBoolAdr());
		}
	}
	
	public void visit(PrintWidth printWidth) {
		Code.loadConst(printWidth.getNum());
	}
	
	public void visit(NoPrintWidth noPrintWidth) {
		Code.loadConst(DEFAULT_PRINT_WIDTH);
	}
	
	//endregion Print
	
	//region Mathematical operations
	
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
}
