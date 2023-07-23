// generated with ast extension for cup
// version 0.8
// 23/6/2023 22:56:3


package rs.ac.bg.etf.pp1.ast;

public class VarDeclNames extends VarDeclNameList {

    private VarDeclNameList VarDeclNameList;
    private VarDeclName VarDeclName;

    public VarDeclNames (VarDeclNameList VarDeclNameList, VarDeclName VarDeclName) {
        this.VarDeclNameList=VarDeclNameList;
        if(VarDeclNameList!=null) VarDeclNameList.setParent(this);
        this.VarDeclName=VarDeclName;
        if(VarDeclName!=null) VarDeclName.setParent(this);
    }

    public VarDeclNameList getVarDeclNameList() {
        return VarDeclNameList;
    }

    public void setVarDeclNameList(VarDeclNameList VarDeclNameList) {
        this.VarDeclNameList=VarDeclNameList;
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
        if(VarDeclNameList!=null) VarDeclNameList.accept(visitor);
        if(VarDeclName!=null) VarDeclName.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclNameList!=null) VarDeclNameList.traverseTopDown(visitor);
        if(VarDeclName!=null) VarDeclName.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclNameList!=null) VarDeclNameList.traverseBottomUp(visitor);
        if(VarDeclName!=null) VarDeclName.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclNames(\n");

        if(VarDeclNameList!=null)
            buffer.append(VarDeclNameList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclName!=null)
            buffer.append(VarDeclName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclNames]");
        return buffer.toString();
    }
}
