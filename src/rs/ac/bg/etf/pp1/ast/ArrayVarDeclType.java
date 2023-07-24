// generated with ast extension for cup
// version 0.8
// 25/6/2023 0:18:47


package rs.ac.bg.etf.pp1.ast;

public class ArrayVarDeclType extends VarDeclKind {

    public ArrayVarDeclType () {
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
        buffer.append("ArrayVarDeclType(\n");

        buffer.append(tab);
        buffer.append(") [ArrayVarDeclType]");
        return buffer.toString();
    }
}
