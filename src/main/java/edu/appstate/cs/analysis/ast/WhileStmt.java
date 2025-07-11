package edu.appstate.cs.analysis.ast;

import edu.appstate.cs.analysis.visitor.AnalysisVisitor;

public class WhileStmt extends Stmt {
    private Expr expr;
    private StmtList stmts;

    public WhileStmt(Expr expr, StmtList stmts) {
        this.expr = expr;
        this.stmts = stmts;
    }

    public Expr getExpr() {
        return expr;
    }

    public StmtList getStmts() {
        return stmts;
    }

    public <R> R accept(AnalysisVisitor<R> analysisVisitor) {
        return analysisVisitor.visitWhileStmt(this);
    }
}