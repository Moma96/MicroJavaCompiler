// generated with ast extension for cup
// version 0.8
// 23/6/2023 15:52:33


package rs.ac.bg.etf.pp1.ast;

public class PrintStatement extends Statement {

    private Factor Factor;
    private PrintParam PrintParam;

    public PrintStatement (Factor Factor, PrintParam PrintParam) {
        this.Factor=Factor;
        if(Factor!=null) Factor.setParent(this);
        this.PrintParam=PrintParam;
        if(PrintParam!=null) PrintParam.setParent(this);
    }

    public Factor getFactor() {
        return Factor;
    }

    public void setFactor(Factor Factor) {
        this.Factor=Factor;
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
        if(Factor!=null) Factor.accept(visitor);
        if(PrintParam!=null) PrintParam.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Factor!=null) Factor.traverseTopDown(visitor);
        if(PrintParam!=null) PrintParam.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Factor!=null) Factor.traverseBottomUp(visitor);
        if(PrintParam!=null) PrintParam.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("PrintStatement(\n");

        if(Factor!=null)
            buffer.append(Factor.toString("  "+tab));
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
