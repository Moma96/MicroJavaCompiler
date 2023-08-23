// generated with ast extension for cup
// version 0.8
// 17/7/2023 0:51:52


package rs.ac.bg.etf.pp1.ast;

public class OneConstAssignment extends ConstAssignmentList {

    private ConstAssignment ConstAssignment;

    public OneConstAssignment (ConstAssignment ConstAssignment) {
        this.ConstAssignment=ConstAssignment;
        if(ConstAssignment!=null) ConstAssignment.setParent(this);
    }

    public ConstAssignment getConstAssignment() {
        return ConstAssignment;
    }

    public void setConstAssignment(ConstAssignment ConstAssignment) {
        this.ConstAssignment=ConstAssignment;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstAssignment!=null) ConstAssignment.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstAssignment!=null) ConstAssignment.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstAssignment!=null) ConstAssignment.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("OneConstAssignment(\n");

        if(ConstAssignment!=null)
            buffer.append(ConstAssignment.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [OneConstAssignment]");
        return buffer.toString();
    }
}
