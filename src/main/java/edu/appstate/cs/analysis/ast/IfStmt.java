package edu.appstate.cs.analysis.ast;

import edu.appstate.cs.analysis.visitor.AnalysisVisitor;

public class IfStmt extends Stmt {
    private Expr condition;
    private StmtList thenBody;
    private ElseIfList elseIfs;
    private StmtList elseBody;

    public IfStmt(Expr condition, StmtList thenBody, ElseIfList elseIfs, StmtList elseBody) {
        this.condition = condition;
        this.thenBody = thenBody;
        this.elseIfs = elseIfs;
        this.elseBody = elseBody;
    }

    public Expr getCondition() {
        return condition;
    }

    public StmtList getThenBody() {
        return thenBody;
    }

    public ElseIfList getElseIfs() {
        return elseIfs;
    }

    public StmtList getElseBody() {
        return elseBody;
    }
    
    public <R> R accept(AnalysisVisitor<R> analysisVisitor) {
        return analysisVisitor.visitIfStmt(this);
    }
}
