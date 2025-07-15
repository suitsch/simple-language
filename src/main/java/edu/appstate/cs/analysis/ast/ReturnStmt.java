package edu.appstate.cs.analysis.ast;

import edu.appstate.cs.analysis.visitor.AnalysisVisitor;

public class ReturnStmt extends Stmt {
    // This is actually quite interesting because I might've missed if we wanted to return values or variables
    // I bring it up because I do not see a ID expression (such as return x;) what does x compute to?
    // It may end up being the IdentExpr
    private Expr retExp;

    public ReturnStmt(Expr ret) {
        this.retExp = ret;
    }

    public Expr getRetExpr() { return this.retExp; }

    // Unsure if we're missing an evaluation method

    public <R> R accpet(AnalysisVisitor<R> analysisVisitor) {
        return analysisVisitor.visitReturnStmt(this);
    }
}