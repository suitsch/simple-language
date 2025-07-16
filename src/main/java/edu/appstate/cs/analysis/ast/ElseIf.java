package edu.appstate.cs.analysis.ast;

import edu.appstate.cs.analysis.visitor.AnalysisVisitor;

public class ElseIf extends Stmt {
    private Expr condition;
    private StmtList body;

    public ElseIf(Expr condition, StmtList body) {
        this.condition = condition;
        this.body = body;
    }

    public Expr getCondition() {
        return condition; }

    public StmtList getBody() { 
        return body; 
    }

    public <R> R accept(AnalysisVisitor<R> analysisVisitor) {
        return analysisVisitor.visitElseIf(this);
    }
}
