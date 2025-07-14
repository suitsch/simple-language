package edu.appstate.cs.analysis.ast;

import edu.appstate.cs.analysis.visitor.AnalysisVisitor;

import java.util.List;

public class ListLit extends Expr {
    private List list;

    public ListLit(List list) {
        this.list = list;
    }

    public ListLit getList() {
        return list;
    }

    public <R> R accept(AnalysisVisitor<R> analysisVisitor) {
        return analysisVisitor.visitListLit(this);
    }
}
