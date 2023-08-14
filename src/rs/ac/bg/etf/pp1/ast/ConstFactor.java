// generated with ast extension for cup
// version 0.8
// 15/7/2023 0:41:39


package rs.ac.bg.etf.pp1.ast;

public class ConstFactor extends Factor {

    private Const Const;

    public ConstFactor (Const Const) {
        this.Const=Const;
        if(Const!=null) Const.setParent(this);
    }

    public Const getConst() {
        return Const;
    }

    public void setConst(Const Const) {
        this.Const=Const;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Const!=null) Const.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Const!=null) Const.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Const!=null) Const.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstFactor(\n");

        if(Const!=null)
            buffer.append(Const.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstFactor]");
        return buffer.toString();
    }
}
