package edu.appstate.cs.analysis.ast;

import edu.appstate.cs.analysis.visitor.AnalysisVisitor;

import java.math.BigInteger;

public class IntLiteral extends Expr {
    private BigInteger num;

    public IntLiteral(BigInteger num) {
        this.num = num;
    }

    public BigInteger getNum() {
        return num;
    }

    public <R> R accept(AnalysisVisitor<R> analysisVisitor) {
        return analysisVisitor.visitIntLiteral(this);
    }
}
