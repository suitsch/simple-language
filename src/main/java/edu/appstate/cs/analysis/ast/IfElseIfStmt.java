package edu.appstate.cs.analysis.ast;

import edu.appstate.cs.analysis.visitor.AnalysisVisitor;

public class IfElseIfStmt extends Stmt { //continue working
    private Expr condition;
    private StmtList stmts;
    private ElseIfList elseifs;

    public IfElseIfStmt( Expr condition, StmtList stmts, ElseIfList elseifs) {
        this.condition = condition;
        this.stmts = stmts;
        this.elseifs = elseifs;
    }

    public Expr getCondition() {
        return condition;
    }

    public StmtList getStmts()
    {
        return stmts;
    }

    public ElseIfList getElseIfs()
    {
        return elseifs;
    }


    public <R> R accept(AnalysisVisitor<R> analysisVisitor) {
        return analysisVisitor.visitIfElseifStmt(this);
    }
}
