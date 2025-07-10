package edu.appstate.cs.analysis.ast;

import edu.appstate.cs.analysis.visitor.AnalysisVisitor;

public class IdentExpr extends Expr {
    private String ident;

    public IdentExpr(String ident) {
        this.ident = ident;
    }

    public String getIdent() {
        return ident;
    }

    public <R> R accept(AnalysisVisitor<R> analysisVisitor) {
        return analysisVisitor.visitIdentExpr(this);
    }
}
