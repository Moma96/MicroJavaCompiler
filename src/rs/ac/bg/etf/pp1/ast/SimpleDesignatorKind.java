// generated with ast extension for cup
// version 0.8
// 15/7/2023 0:41:39


package rs.ac.bg.etf.pp1.ast;

public class SimpleDesignatorKind extends DesignatorKind {

    public SimpleDesignatorKind () {
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SimpleDesignatorKind(\n");

        buffer.append(tab);
        buffer.append(") [SimpleDesignatorKind]");
        return buffer.toString();
    }
}