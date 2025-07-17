package edu.appstate.cs.analysis.ast;

import java.util.ArrayList;
import java.util.Iterator;

import beaver.Symbol;
import edu.appstate.cs.analysis.visitor.AnalysisVisitor;

public class ExprList extends Symbol implements Iterable<Expr> {
    private ArrayList<Expr> exprList;

    public ExprList() {
        exprList = new ArrayList<>();
    }

    public ExprList(Expr e) {
        exprList = new ArrayList<>();
        exprList.add(e);
    }

    public ExprList(Expr e, ExprList es) {
        exprList = new ArrayList<>();
        exprList.add(e);
        exprList.addAll(es.exprList);
    }

    @Override
    public Iterator<Expr> iterator() {
        return exprList.iterator();
    }

    public <R> R accept(AnalysisVisitor<R> analysisVisitor) {
        return analysisVisitor.visitExprList(this);
    }

}
