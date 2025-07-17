package edu.appstate.cs.analysis.ast;

import edu.appstate.cs.analysis.visitor.AnalysisVisitor;

public class NegExpr extends Expr {
    private Expr expr;

    public NegExpr(Expr expr) {
        this.expr = expr;
    }

    public Expr getExpr() {
        return expr;
    }

    public <R> R accept(AnalysisVisitor<R> analysisVisitor) {
        return analysisVisitor.visitNegExpr(this);
    }

}
