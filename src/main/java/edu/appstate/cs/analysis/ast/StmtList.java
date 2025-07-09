package edu.appstate.cs.analysis.ast;

import java.util.ArrayList;
import java.util.Iterator;

import beaver.Symbol;
import edu.appstate.cs.analysis.visitor.AnalysisVisitor;

public class StmtList extends Symbol implements Iterable<Stmt> {
    private ArrayList<Stmt> stmtList;

    public StmtList() {
        stmtList = new ArrayList<>();
    }

    public StmtList(Stmt s) {
        stmtList = new ArrayList<>();
        stmtList.add(s);
    }

    public StmtList(Stmt s, StmtList ss) {
        stmtList = new ArrayList<>();
        stmtList.add(s);
        stmtList.addAll(ss.stmtList);
    }

    @Override
    public Iterator<Stmt> iterator() {
        return stmtList.iterator();
    }

    public <R> R accept(AnalysisVisitor<R> analysisVisitor) {
        return analysisVisitor.visitStmtList(this);
    }

}
