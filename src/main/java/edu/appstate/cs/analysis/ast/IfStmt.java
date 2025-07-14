package edu.appstate.cs.analysis.ast;
import edu.appstate.cs.analysis.visitor.AnalysisVisitor;

public class IfStmt extends Stmt {
    private Expr condition;
    private StmtList thenBody;
    private ElseIfList elseIfs;

    public IfStmt(Expr condition, StmtList thenBody, ElseIfList elseIfs) {
        this.condition = condition;
        this.thenBody = thenBody;
        this.elseIfs = elseIfs;
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


    public <R> R accept(AnalysisVisitor<R> analysisVisitor) {
        return analysisVisitor.visitIfStmt(this);
    }
}