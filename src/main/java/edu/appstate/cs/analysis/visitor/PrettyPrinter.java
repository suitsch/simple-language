package edu.appstate.cs.analysis.visitor;

import edu.appstate.cs.analysis.ast.AssignStmt;
import edu.appstate.cs.analysis.ast.IntLiteral;
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
}
