package edu.appstate.cs.analysis.cfg;

import edu.appstate.cs.analysis.ast.Expr;
import edu.appstate.cs.analysis.ast.Stmt;
import edu.appstate.cs.analysis.visitor.AnalysisVisitor;

import java.util.Objects;

public abstract class Node {
    public <R> R accept(AnalysisVisitor<R> analysisVisitor) {
        return null; // This should never be called
    }

    public static class ExprNode extends Node {
        private Expr expr;
        public ExprNode(Expr expr) {
            this.expr = expr;
        }
        public Expr getExpr() {
            return expr;
        }
        public <R> R accept(AnalysisVisitor<R> analysisVisitor) {
            return expr.accept(analysisVisitor);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ExprNode) {
                ExprNode other = (ExprNode) obj;
                return expr.equals(other.expr);
            }
            return super.equals(obj);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(expr);
        }
    }

    public static class StmtNode extends Node {
        private Stmt stmt;
        public StmtNode(Stmt stmt) {
            this.stmt = stmt;
        }
        public Stmt getStmt() {
            return stmt;
        }
        public <R> R accept(AnalysisVisitor<R> analysisVisitor) {
            return stmt.accept(analysisVisitor);
        }
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof StmtNode) {
                StmtNode other = (StmtNode) obj;
                return stmt.equals(other.stmt);
            }
            return super.equals(obj);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(stmt);
        }
    }
}
