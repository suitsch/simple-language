package edu.appstate.cs.analysis.ast;

import edu.appstate.cs.analysis.visitor.AnalysisVisitor;

public class IfElseIfStmt extends Stmt { //continue working
    private Expr expr;
    private ElseIfList elseifs;

    public IfElseIfStmt( Expr expr1, Expr expr, ElseIfList elseifs,StmtList ss, StmtList) {
        this.expr1 = expr1;
        this.elseifs = elseifs;
    }



    public Expr getExpr1() {
        return expr1;
    }

    public Expr getExpr2()
    {
        return expr2;
    }


    public <R> R accept(AnalysisVisitor<R> analysisVisitor) {
        return analysisVisitor.visitIfElseifStmt(this);
    }
}
