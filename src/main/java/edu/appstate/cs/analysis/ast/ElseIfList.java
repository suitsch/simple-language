package edu.appstate.cs.analysis.ast;

import java.util.ArrayList;
import java.util.Iterator;

import beaver.Symbol;
import edu.appstate.cs.analysis.visitor.AnalysisVisitor;

public class ElseIfList extends Symbol implements Iterable<ElseIf> {
    private ArrayList<ElseIf> elseifList;

    public ElseIfList() {
        elseifList = new ArrayList<>();
    }

    public ElseIfList(ElseIf s) {
        elseifList = new ArrayList<>();
        elseifList.add(s);
    }

    public ElseIfList(ElseIf s, ElseIfList ee) {
        elseifList = new ArrayList<>();
        elseifList.add(s);
        elseifList.addAll(ee.elseifList);
    }

    @Override
    public Iterator<ElseIf> iterator() {
        return elseifList.iterator();
    }

    public <R> R accept(AnalysisVisitor<R> analysisVisitor) {
        return analysisVisitor.visitElseIfList(this);
    }

}