// generated with ast extension for cup
// version 0.8
// 15/7/2023 0:41:39


package rs.ac.bg.etf.pp1.ast;

public interface Visitor { 

    public void visit(Factor Factor);
    public void visit(Assignop Assignop);
    public void visit(Mulop Mulop);
    public void visit(DesignatorStatement DesignatorStatement);
    public void visit(Expr Expr);
    public void visit(Const Const);
    public void visit(VarDeclList VarDeclList);
    public void visit(ConstDeclList ConstDeclList);
    public void visit(VarDeclNameList VarDeclNameList);
    public void visit(Addop Addop);
    public void visit(MethodDeclList MethodDeclList);
    public void visit(PrintParam PrintParam);
    public void visit(Statement Statement);
    public void visit(Term Term);
    public void visit(ConstAssignmentList ConstAssignmentList);
    public void visit(StatementList StatementList);
    public void visit(VarDeclKind VarDeclKind);
    public void visit(DesignatorKind DesignatorKind);
    public void visit(RightHandSide RightHandSide);
    public void visit(Type Type);
    public void visit(BoolConst BoolConst);
    public void visit(CharConst CharConst);
    public void visit(NumConst NumConst);
    public void visit(NoPrintWidth NoPrintWidth);
    public void visit(PrintWidth PrintWidth);
    public void visit(ElemDesignKindFirstNode ElemDesignKindFirstNode);
    public void visit(SimpleDesignatorKind SimpleDesignatorKind);
    public void visit(ElemDesignatorKind ElemDesignatorKind);
    public void visit(Designator Designator);
    public void visit(ArrayFactor ArrayFactor);
    public void visit(ParenFactor ParenFactor);
    public void visit(ConstFactor ConstFactor);
    public void visit(DesignatorFactor DesignatorFactor);
    public void visit(Mod Mod);
    public void visit(Div Div);
    public void visit(Mul Mul);
    public void visit(Minus Minus);
    public void visit(Plus Plus);
    public void visit(TermFactor TermFactor);
    public void visit(MulopTerm MulopTerm);
    public void visit(ExprTerm ExprTerm);
    public void visit(ExprNegTerm ExprNegTerm);
    public void visit(AddopExpr AddopExpr);
    public void visit(RightHandFindAny RightHandFindAny);
    public void visit(RightHandExpression RightHandExpression);
    public void visit(DecStatement DecStatement);
    public void visit(IncStatement IncStatement);
    public void visit(Assignment Assignment);
    public void visit(PrintStatement PrintStatement);
    public void visit(DesignatorSemiStatement DesignatorSemiStatement);
    public void visit(ErrorStatement ErrorStatement);
    public void visit(NoStatement NoStatement);
    public void visit(Statements Statements);
    public void visit(MethodTypeName MethodTypeName);
    public void visit(MethodDecl MethodDecl);
    public void visit(NoMethodDeclarations NoMethodDeclarations);
    public void visit(MethodDeclarations MethodDeclarations);
    public void visit(SimpleVarDeclKind SimpleVarDeclKind);
    public void visit(ArrayVarDeclKind ArrayVarDeclKind);
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
