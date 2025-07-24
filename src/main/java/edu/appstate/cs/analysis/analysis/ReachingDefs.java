package edu.appstate.cs.analysis.analysis;

import edu.appstate.cs.analysis.ast.*;
import edu.appstate.cs.analysis.cfg.CFG;
import edu.appstate.cs.analysis.visitor.AnalysisVisitor;

import java.util.*;

public class ReachingDefs {
    private CFG cfg = null;

    public ReachingDefs(CFG cfg) {
        this.cfg = cfg;
    }

    public static class Def {
        private String name;
        private String loc;
        public Def(String name, String loc) {
            this.name = name;
            this.loc = loc;
        }

        @Override
        public String toString() {
            return String.format("[name: %s, loc: %s]", name, loc);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Def def = (Def) o;
            return Objects.equals(name, def.name) && Objects.equals(loc, def.loc);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, loc);
        }
    }

    public Map<String, Set<Def>> computeDefs() {
        Map<String, Set<Def>> reachingDefs = new HashMap<>();

        // TODO: We will add the analysis here!

        // Step 1: Compute gen, we need a visitor for this!

        // Step 2: Compute defs, this can be based on gen

        // Step 3: Compute kill based on gen and defs

        // Step 4: Use this info to compute reach. We will do this
        // and iterate until the info stabilizes.

        return reachingDefs;
    }

    // What goes here???
    private static class GenVisitor implements AnalysisVisitor<Set<Def>> {

        @Override
        public Set<Def> visitStmtList(StmtList stmtList) {
            Set<Def> defs = new HashSet<>();
            for (Stmt stmt : stmtList) {
                defs.addAll(stmt.accept(this));
            }
            return defs;
        }

        @Override
        public Set<Def> visitElseIfList(ElseIfList elseIfList) {
            Set<Def> defs = new HashSet<>();
            for (ElseIf elseIf : elseIfList) {
                defs.addAll(elseIf.accept(this));
            }
            return defs;
        }

        @Override
        public Set<Def> visitExprList(ExprList exprList) {
            Set<Def> defs = new HashSet<>();
            for (Expr expr : exprList) {
                defs.addAll(expr.accept(this));
            }
            return defs;
        }

        @Override
        public Set<Def> visitElseIf(ElseIf elseIf) {
            Set<Def> defs = new HashSet<>();
            defs.addAll(elseIf.getCondition().accept(this));
            defs.addAll(elseIf.getBody().accept(this));
            return defs;
        }

        @Override
        public Set<Def> visitIfStmt(IfStmt ifStmt) {
            return Set.of();
        }

        @Override
        public Set<Def> visitAssignStmt(AssignStmt assignStmt) {
            return Set.of();
        }

        @Override
        public Set<Def> visitIntLiteral(IntLiteral intLiteral) {
            return Set.of();
        }

        @Override
        public Set<Def> visitBooleanLiteral(BooleanLiteral booleanLiteral) {
            return Set.of();
        }

        @Override
        public Set<Def> visitPlusExpr(PlusExpr plusExpr) {
            return Set.of();
        }

        @Override
        public Set<Def> visitSubExpr(SubExpr subExpr) {
            return Set.of();
        }

        @Override
        public Set<Def> visitMultExpr(MultExpr multExpr) {
            return Set.of();
        }

        @Override
        public Set<Def> visitDivExpr(DivExpr divExpr) {
            return Set.of();
        }

        @Override
        public Set<Def> visitIdentExpr(IdentExpr identExpr) {
            return Set.of();
        }

        @Override
        public Set<Def> visitForStmt(ForStmt forStmt) {
            return Set.of();
        }

        @Override
        public Set<Def> visitWhileStmt(WhileStmt whileStmt) {
            return Set.of();
        }

        @Override
        public Set<Def> visitExprStmt(ExprStmt exprStmt) {
            return Set.of();
        }

        @Override
        public Set<Def> visitNotEqlExpr(NotEqlExpr notEqlExpr) {
            return Set.of();
        }

        @Override
        public Set<Def> visitEqualExpr(EqualExpr equalExpr) {
            return Set.of();
        }

        @Override
        public Set<Def> visitDeclStmt(DeclStmt declStmt) {
            return Set.of();
        }

        @Override
        public Set<Def> visitNotExpr(NotExpr notExpr) {
            return Set.of();
        }

        @Override
        public Set<Def> visitReturnStmt(ReturnStmt returnStmt) {
            return Set.of();
        }

        @Override
        public Set<Def> visitOrExpr(OrExpr orExpr) {
            return Set.of();
        }

        @Override
        public Set<Def> visitAndExpr(AndExpr andExpr) {
            return Set.of();
        }

        @Override
        public Set<Def> visitLtExpr(LtExpr ltExpr) {
            return Set.of();
        }

        @Override
        public Set<Def> visitLtEqExpr(LtEqExpr ltEqExpr) {
            return Set.of();
        }

        @Override
        public Set<Def> visitGtExpr(GtExpr gtExpr) {
            return Set.of();
        }

        @Override
        public Set<Def> visitGtEqExpr(GtEqExpr gtEqExpr) {
            return Set.of();
        }

        @Override
        public Set<Def> visitListExpr(ListExpr listExpr) {
            return Set.of();
        }

        @Override
        public Set<Def> visitNegExpr(NegExpr negExpr) {
            return Set.of();
        }
    }
}
