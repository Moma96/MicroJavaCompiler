// generated with ast extension for cup
// version 0.8
// 23/6/2023 16:33:57


package rs.ac.bg.etf.pp1.ast;

public interface Visitor { 

    public void visit(Factor Factor);
    public void visit(Assignop Assignop);
    public void visit(Const Const);
    public void visit(VarDeclList VarDeclList);
    public void visit(ConstDeclList ConstDeclList);
    public void visit(VarDeclNameList VarDeclNameList);
    public void visit(MethodDeclList MethodDeclList);
    public void visit(PrintParam PrintParam);
    public void visit(Statement Statement);
    public void visit(Term Term);
    public void visit(ConstAssignmentList ConstAssignmentList);
    public void visit(StatementList StatementList);
    public void visit(Type Type);
    public void visit(BoolConst BoolConst);
    public void visit(CharConst CharConst);
    public void visit(NumConst NumConst);
    public void visit(NoPrintWidth NoPrintWidth);
    public void visit(PrintWidth PrintWidth);
    public void visit(Designator Designator);
    public void visit(ConstFactor ConstFactor);
    public void visit(DesignatorFactor DesignatorFactor);
    public void visit(PrintStatement PrintStatement);
    public void visit(Assignment Assignment);
    public void visit(ErrorStatement ErrorStatement);
    public void visit(NoStatement NoStatement);
    public void visit(Statements Statements);
    public void visit(MethodTypeName MethodTypeName);
    public void visit(MethodDecl MethodDecl);
    public void visit(NoMethodDeclarations NoMethodDeclarations);
    public void visit(MethodDeclarations MethodDeclarations);
    public void visit(VarDeclName VarDeclName);
    public void visit(OneVarDeclName OneVarDeclName);
    public void visit(VarDeclNames VarDeclNames);
    public void visit(VarDecl VarDecl);
    public void visit(NoVarDeclaration NoVarDeclaration);
    public void visit(VarDeclarations VarDeclarations);
    public void visit(ConstAssignment ConstAssignment);
    public void visit(OneConstAssignment OneConstAssignment);
    public void visit(ConstAssignments ConstAssignments);
    public void visit(ConstDecl ConstDecl);
    public void visit(NoConstDeclaration NoConstDeclaration);
    public void visit(ConstDeclarations ConstDeclarations);
    public void visit(ProgramName ProgramName);
    public void visit(Program Program);

}
