// generated with ast extension for cup
// version 0.8
// 23/6/2023 18:34:52


package rs.ac.bg.etf.pp1.ast;

public class PrintStatement extends Statement {

    private Term Term;
    private PrintParam PrintParam;

    public PrintStatement (Term Term, PrintParam PrintParam) {
        this.Term=Term;
        if(Term!=null) Term.setParent(this);
        this.PrintParam=PrintParam;
        if(PrintParam!=null) PrintParam.setParent(this);
    }

    public Term getTerm() {
        return Term;
    }

    public void setTerm(Term Term) {
        this.Term=Term;
    }

    public PrintParam getPrintParam() {
        return PrintParam;
    }

    public void setPrintParam(PrintParam PrintParam) {
        this.PrintParam=PrintParam;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Term!=null) Term.accept(visitor);
        if(PrintParam!=null) PrintParam.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Term!=null) Term.traverseTopDown(visitor);
        if(PrintParam!=null) PrintParam.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Term!=null) Term.traverseBottomUp(visitor);
        if(PrintParam!=null) PrintParam.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("PrintStatement(\n");

        if(Term!=null)
            buffer.append(Term.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(PrintParam!=null)
            buffer.append(PrintParam.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [PrintStatement]");
        return buffer.toString();
    }
}
