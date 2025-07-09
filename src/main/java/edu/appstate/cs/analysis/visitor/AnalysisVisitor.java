package edu.appstate.cs.analysis.visitor;

import edu.appstate.cs.analysis.ast.*;

public interface AnalysisVisitor<R> {
    // TODO: Add additional visit methods as we add AST classes
    R visitStmtList(StmtList stmtList);
    R visitAssignStmt(AssignStmt assignStmt);
    R visitIntLiteral(IntLiteral intLiteral);
}
