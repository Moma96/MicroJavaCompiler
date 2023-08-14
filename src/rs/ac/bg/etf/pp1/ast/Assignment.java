// generated with ast extension for cup
// version 0.8
// 15/7/2023 0:41:39


package rs.ac.bg.etf.pp1.ast;

public class Assignment extends DesignatorStatement {

    private Designator Designator;
    private RightHandSide RightHandSide;

    public Assignment (Designator Designator, RightHandSide RightHandSide) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.RightHandSide=RightHandSide;
        if(RightHandSide!=null) RightHandSide.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public RightHandSide getRightHandSide() {
        return RightHandSide;
    }

    public void setRightHandSide(RightHandSide RightHandSide) {
        this.RightHandSide=RightHandSide;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(RightHandSide!=null) RightHandSide.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(RightHandSide!=null) RightHandSide.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(RightHandSide!=null) RightHandSide.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Assignment(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(RightHandSide!=null)
            buffer.append(RightHandSide.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Assignment]");
        return buffer.toString();
    }
}
