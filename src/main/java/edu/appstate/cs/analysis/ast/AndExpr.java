package edu.appstate.cs.analysis.ast;

import edu.appstate.cs.analysis.visitor.AnalysisVisitor;

public class AndStmt extends Stmt {
    private Expr left;
    private Expr right;

    
    public AndStmt(Expr right, Expr left) {
        this.left = left;
        this.right = right;
    }


    public Expr getLeft() {
        return left;
    }

    public StmtList getRight() {
        return right;
    }

    public <R> R accept(AnalysisVisitor<R> analysisVisitor) {
        return analysisVisitor.visitAndExpr(this);
    }
}