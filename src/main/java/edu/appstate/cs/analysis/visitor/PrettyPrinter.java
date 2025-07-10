package edu.appstate.cs.analysis.visitor;

import edu.appstate.cs.analysis.ast.AssignStmt;
import edu.appstate.cs.analysis.ast.IntLiteral;
import edu.appstate.cs.analysis.ast.MultExpr;
import edu.appstate.cs.analysis.ast.PlusExpr;
import edu.appstate.cs.analysis.ast.Stmt;
import edu.appstate.cs.analysis.ast.StmtList;

public class PrettyPrinter implements AnalysisVisitor<String> {
    @Override
    public String visitStmtList(StmtList stmtList) {
        StringBuffer buf = new StringBuffer();
        for (Stmt stmt : stmtList) {
            buf.append(stmt.accept(this)).append("\n");
        }
        return buf.toString();
    }

    @Override
    public String visitAssignStmt(AssignStmt assignStmt) {
        return String.format("%s = %s;", assignStmt.getIdent(), assignStmt.getExpr().accept(this));
    }

    @Override
    public String visitIntLiteral(IntLiteral intLiteral) {
        return intLiteral.getNum().toString();
    }

    @Override
    public String visitPlusExpr(PlusExpr plusExpr) {
        return String.format("%s + %s", plusExpr.getLeft().accept(this), plusExpr.getRight().accept(this));
    }

    @Override
    public String visitMultExpr(MultExpr multExpr) {
        return String.format("%s * %s", multExpr.getLeft().accept(this), multExpr.getRight().accept(this));
    }
}
