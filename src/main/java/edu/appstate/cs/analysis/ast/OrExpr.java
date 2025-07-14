package edu.appstate.cs.analysis.ast;

import edu.appstate.cs.analysis.visitor.AnalysisVisitor;

public class OrExpr extends Expr {
    private Expr leftExpr;
    private Expr rightExpr;

    public OrExpr(Expr left, Expr right) {
        this.leftExpr = left;
        this.rightExpr = right;
    }

    public Expr getLeftExpr() { return this.leftExpr; }

    public Expr getRightExpr() { return this.rightExpr; }

    public <R> R accept(AnalysisVisitor<R> analysisVisitor) {
        return AnalysisVisitor.visitOrExpr(this);
    }
}