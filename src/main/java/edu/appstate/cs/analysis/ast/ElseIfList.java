package edu.appstate.cs.analysis.ast;

import java.util.ArrayList;
import java.util.Iterator;

import beaver.Symbol;
import edu.appstate.cs.analysis.visitor.AnalysisVisitor;

public class ElseIfList extends Symbol implements Iterable<ElseIf> {
    private ArrayList<ElseIf> elseIfList;

    public ElseIfList() {
        elseIfList = new ArrayList<>();
    }

    public ElseIfList(ElseIf ei) {
        elseIfList = new ArrayList<>();
        elseIfList.add(ei);
    }

    public ElseIfList(ElseIf ei, ElseIfList eis) {
        elseIfList = new ArrayList<>();
        elseIfList.add(ei);
        elseIfList.addAll(eis.elseIfList);
    }

    @Override
    public Iterator<ElseIf> iterator() {
        return elseIfList.iterator();
    }

    public <R> R accept(AnalysisVisitor<R> analysisVisitor) {
        return analysisVisitor.visitElseIfList(this);
    }

}
