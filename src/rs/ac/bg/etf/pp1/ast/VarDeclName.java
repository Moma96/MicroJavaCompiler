// generated with ast extension for cup
// version 0.8
// 15/7/2023 0:41:39


package rs.ac.bg.etf.pp1.ast;

public class VarDeclName implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Obj obj = null;

    private String varName;
    private VarDeclKind VarDeclKind;

    public VarDeclName (String varName, VarDeclKind VarDeclKind) {
        this.varName=varName;
        this.VarDeclKind=VarDeclKind;
        if(VarDeclKind!=null) VarDeclKind.setParent(this);
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName=varName;
    }

    public VarDeclKind getVarDeclKind() {
        return VarDeclKind;
    }

    public void setVarDeclKind(VarDeclKind VarDeclKind) {
        this.VarDeclKind=VarDeclKind;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarDeclKind!=null) VarDeclKind.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclKind!=null) VarDeclKind.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclKind!=null) VarDeclKind.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclName(\n");

        buffer.append(" "+tab+varName);
        buffer.append("\n");

        if(VarDeclKind!=null)
            buffer.append(VarDeclKind.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclName]");
        return buffer.toString();
    }
}
