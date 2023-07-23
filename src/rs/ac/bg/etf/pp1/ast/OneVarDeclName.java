// generated with ast extension for cup
// version 0.8
// 23/6/2023 16:33:57


package rs.ac.bg.etf.pp1.ast;

public class OneVarDeclName extends VarDeclNameList {

    private VarDeclName VarDeclName;

    public OneVarDeclName (VarDeclName VarDeclName) {
        this.VarDeclName=VarDeclName;
        if(VarDeclName!=null) VarDeclName.setParent(this);
    }

    public VarDeclName getVarDeclName() {
        return VarDeclName;
    }

    public void setVarDeclName(VarDeclName VarDeclName) {
        this.VarDeclName=VarDeclName;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarDeclName!=null) VarDeclName.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclName!=null) VarDeclName.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclName!=null) VarDeclName.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("OneVarDeclName(\n");

        if(VarDeclName!=null)
            buffer.append(VarDeclName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [OneVarDeclName]");
        return buffer.toString();
    }
}
