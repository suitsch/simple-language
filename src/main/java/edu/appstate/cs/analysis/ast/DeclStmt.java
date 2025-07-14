package edu.appstate.cs.analysis.ast;

import edu.appstate.cs.analysis.visitor.AnalysisVisitor;

public class DeclStmt extends Stmt {
    private final String ident;
    private final Expr expr;

    public DeclStmt(String ident, Expr expr) {
        this.ident = ident;
        this.expr = expr;
    }

    public String getIdent() {
        return ident;
    }

    public Expr getExpr() {
        return expr;
    }

    @Override
    public <R> R accept(AnalysisVisitor<R> visitor) {
        return visitor.visitDeclStmt(this);
    }
    
}
