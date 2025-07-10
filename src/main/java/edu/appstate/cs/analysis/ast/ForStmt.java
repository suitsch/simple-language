package edu.appstate.cs.analysis.ast;

import edu.appstate.cs.analysis.visitor.AnalysisVisitor;

public class ForStmt extends Stmt {
    private String ident;
    private Expr expr;
    private StmtList stmts;

    public ForStmt(String ident, Expr expr, StmtList stmts) {
        this.ident = ident;
        this.expr = expr;
        this.stmts = stmts;
    }

    public String getIdent() {
        return ident;
    }

    public Expr getExpr() {
        return expr;
    }

    public StmtList getStmts() {
        return stmts;
    }

    public <R> R accept(AnalysisVisitor<R> analysisVisitor) {
        return analysisVisitor.visitForStmt(this);
    }
}
