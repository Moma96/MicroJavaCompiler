// generated with ast extension for cup
// version 0.8
// 26/6/2023 23:1:16


package rs.ac.bg.etf.pp1.ast;

public class ElemDesignatorKind extends DesignatorKind {

    private ElemDesignKindFirstNode ElemDesignKindFirstNode;
    private Expr Expr;

    public ElemDesignatorKind (ElemDesignKindFirstNode ElemDesignKindFirstNode, Expr Expr) {
        this.ElemDesignKindFirstNode=ElemDesignKindFirstNode;
        if(ElemDesignKindFirstNode!=null) ElemDesignKindFirstNode.setParent(this);
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
    }

    public ElemDesignKindFirstNode getElemDesignKindFirstNode() {
        return ElemDesignKindFirstNode;
    }

    public void setElemDesignKindFirstNode(ElemDesignKindFirstNode ElemDesignKindFirstNode) {
        this.ElemDesignKindFirstNode=ElemDesignKindFirstNode;
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ElemDesignKindFirstNode!=null) ElemDesignKindFirstNode.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ElemDesignKindFirstNode!=null) ElemDesignKindFirstNode.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ElemDesignKindFirstNode!=null) ElemDesignKindFirstNode.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ElemDesignatorKind(\n");

        if(ElemDesignKindFirstNode!=null)
            buffer.append(ElemDesignKindFirstNode.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ElemDesignatorKind]");
        return buffer.toString();
    }
}
