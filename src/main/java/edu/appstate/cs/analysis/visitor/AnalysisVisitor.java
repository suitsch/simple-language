package edu.appstate.cs.analysis.visitor;

import edu.appstate.cs.analysis.ast.*;

public interface AnalysisVisitor<R> {
    // TODO: Add additional visit methods as we add AST classes
    R visitStmtList(StmtList stmtList);

    R visitElseIfList(ElseIfList elseIfList);

    R visitElseIf(ElseIf elseIf);

    R visitIfStmt(IfStmt ifStmt);

    R visitAssignStmt(AssignStmt assignStmt);

    R visitIntLiteral(IntLiteral intLiteral);

    R visitBooleanLiteral(BooleanLiteral booleanLiteral);

    R visitPlusExpr(PlusExpr plusExpr);

    R visitSubExpr(SubExpr subExpr);

    R visitMultExpr(MultExpr multExpr);

    R visitDivExpr(DivExpr divExpr);

    R visitIdentExpr(IdentExpr identExpr);

    R visitForStmt(ForStmt forStmt);

    R visitWhileStmt(WhileStmt whileStmt);

    R visitExprStmt(ExprStmt exprStmt);

    R visitNotEqlExpr(NotEqlExpr notEqlExpr);

    R visitEqualExpr(EqualExpr equalExpr);

    R visitDeclStmt(DeclStmt declStmt);

    R visitNotExpr(NotExpr notExpr);

    R visitReturnStmt(ReturnStmt returnStmt);

    R visitOrExpr(OrExpr orExpr);

    R visitAndExpr(AndExpr andExpr);

    R visitLtExpr(LtExpr ltExpr);

    R visitLtEqExpr(LtEqExpr ltEqExpr);

    R visitGtExpr(GtExpr gtExpr);

    R visitGtEqExpr(GtEqExpr gtEqExpr);
}
