package edu.appstate.cs.analysis.ast;

import edu.appstate.cs.analysis.visitor.AnalysisVisitor;

public class IfElseStmt extends Stmt {
    private Expr expr;
    private StmtList stmts1;
    private StmtList stmts2;

    public IfElseStmt(Expr e, StmtList st1, StmtList st2) {
        this.expr = e;
        this.stmts1 = st1;
        this.stmts2 = st2;
    }

    public Expr getExpr() {
        return expr;
    }

    public StmtList getStmts1() {
        return stmts1;
    }

    public StmtList getStmts2() {
        return stmts2;
    }

    public <R> R accept(AnalysisVisitor<R> analysisVisitor) {
        return analysisVisitor.visitIfElseStmt(this);
    }
}
