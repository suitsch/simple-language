package edu.appstate.cs.analysis.ast;

import edu.appstate.cs.analysis.visitor.AnalysisVisitor;

public class GtEqExpr extends Expr {
    private Expr left;
    private Expr right;

    public GtEqExpr(Expr left, Expr right) {
        this.left = left;
        this.right = right;
    }

    public Expr getLeft() {
        return left;
    }

    public Expr getRight() {
        return right;
    }

    public <R> R accept(AnalysisVisitor<R> analysisVisitor) {
        return analysisVisitor.visitGtEqExpr(this);
    }
}
