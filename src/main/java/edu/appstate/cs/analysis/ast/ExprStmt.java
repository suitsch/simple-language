package edu.appstate.cs.analysis.ast;

import edu.appstate.cs.analysis.visitor.AnalysisVisitor;

public class ExprStmt extends Stmt {
    private Expr expr;

    public ExprStmt(Expr expr) {
        this.expr = expr;
    }

    public Expr getExpr() {
        return expr;
    }

    public <R> R accept(AnalysisVisitor<R> analysisVisitor) {
        return analysisVisitor.visitExprStmt(this);
    }

}
