package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.VarDeclName;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;

public class CounterVisitor extends VisitorAdaptor {
	
	protected int count;
	
	public int getCount() {
		return count;
	}
	
	public static class VarCounter extends CounterVisitor {
		
		public void visit(VarDeclName formParamDecl) {
			count++;
		}
	}
}
