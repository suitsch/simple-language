package edu.appstate.cs.analysis.ast;

import edu.appstate.cs.analysis.visitor.AnalysisVisitor;

public class IfElseIfStmt extends Stmt {
    private Expr condition;
    private StmtList ifBody;

    public IfElseIfStmt(String ident, Expr condition, StmtList stmts) {
        this.ident = ident;
        this.condition = condition;
        this.stmts = stmts;
    }



    public Expr getCondition() {
        return condition;
    }


    public <R> R accept(AnalysisVisitor<R> analysisVisitor) {
        return analysisVisitor.visitIfElseifStmt(this);
    }
}
