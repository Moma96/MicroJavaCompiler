// generated with ast extension for cup
// version 0.8
// 20/6/2023 3:33:5


package rs.ac.bg.etf.pp1.ast;

public interface Visitor { 

    public void visit(Designator Designator);
    public void visit(Factor Factor);
    public void visit(ActualParamList ActualParamList);
    public void visit(FormalParamList FormalParamList);
    public void visit(Type Type);
    public void visit(FormPars FormPars);
    public void visit(VarDeclList VarDeclList);
    public void visit(VarDecl VarDecl);
    public void visit(Unmatched Unmatched);
    public void visit(FormalParamDecl FormalParamDecl);
    public void visit(MethodDeclList MethodDeclList);
    public void visit(Statement Statement);
    public void visit(Term Term);
    public void visit(StatementList StatementList);
    public void visit(Matched Matched);
    public void visit(ProgName ProgName);
    public void visit(ActualPars ActualPars);
    public void visit(BoolConst BoolConst);
    public void visit(CharConst CharConst);
    public void visit(NumConst NumConst);
    public void visit(PrintStatement PrintStatement);
    public void visit(ErrorStatement ErrorStatement);
    public void visit(NoStatement NoStatement);
    public void visit(Statements Statements);
    public void visit(MethodTypeName MethodTypeName);
    public void visit(MethodDecl MethodDecl);
    public void visit(NoMethodDecl NoMethodDecl);
    public void visit(MethodDeclarations MethodDeclarations);
    public void visit(Program Program);

}
