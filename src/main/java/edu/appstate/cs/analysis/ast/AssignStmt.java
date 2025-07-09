package edu.appstate.cs.analysis.ast;

import edu.appstate.cs.analysis.visitor.AnalysisVisitor;

public class AssignStmt extends Stmt {
    private String ident;
    private Expr expr;

    public AssignStmt(String ident, Expr expr) {
        this.ident = ident;
        this.expr = expr;
    }

    public String getIdent() {
        return ident;
    }

    public Expr getExpr() {
        return expr;
    }

    public <R> R accept(AnalysisVisitor<R> analysisVisitor) {
        return analysisVisitor.visitAssignStmt(this);
    }
}
