package edu.appstate.cs.analysis.ast;

import edu.appstate.cs.analysis.visitor.AnalysisVisitor;

import java.util.List;

public class ListExpr extends Expr {
    private ExprList list;

    public ListExpr(ExprList list) {
        this.list = list;
    }

    public ExprList getList() {
        return list;
    }

    public <R> R accept(AnalysisVisitor<R> analysisVisitor) {
        return analysisVisitor.visitListExpr(this);
    }
}
