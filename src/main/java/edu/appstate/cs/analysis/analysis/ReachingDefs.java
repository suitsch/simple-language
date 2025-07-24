package edu.appstate.cs.analysis.analysis;

import edu.appstate.cs.analysis.ast.*;
import edu.appstate.cs.analysis.cfg.CFG;
import edu.appstate.cs.analysis.cfg.Node;
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
        Map<String, Set<Def>> gen = new HashMap<>();
        GenVisitor genVisitor = new GenVisitor();
        for (Node n : cfg.getNodes()) {
            gen.put(cfg.getNodeId(n), n.accept(genVisitor));
        }

        // Step 2: Compute defs, this can be based on gen
        Map<String, Set<Def>> defs = new HashMap<>();
        for (String nodeId : gen.keySet()) {
            for (Def def : gen.get(nodeId)) {
                if (! defs.containsKey(def.name)) {
                    defs.put(def.name, new HashSet<>());
                }
                defs.get(def.name).add(def);
            }
        }

        // Step 3: Compute kill based on gen and defs
        Map<String, Set<Def>> kills = new HashMap<>();
        for (String nodeId : gen.keySet()) {
            // Store all the defs killed at this node
            Set<Def> nodeKills = new HashSet<>();

            // If we generated anything in this node, use that to compute kills
            if (gen.get(nodeId).size() > 0) {
                for (Def def : gen.get(nodeId)) {
                    // Given a definition of a name n, we kill all other definitions of n
                    Set<Def> killSet = Set.copyOf(defs.get(def.name));
                    killSet.remove(def);
                    nodeKills.addAll(killSet);
                }
            }
            kills.put(nodeId, nodeKills);
        }

        // Step 4: Use this info to compute reach. We will do this
        // and iterate until the info stabilizes.

        return reachingDefs;
    }

    // What goes here???
    private static class GenVisitor implements AnalysisVisitor<Set<Def>> {

        @Override
        public Set<Def> visitStmtList(StmtList stmtList) {
            return Set.of();        }

        @Override
        public Set<Def> visitElseIfList(ElseIfList elseIfList) {
            return Set.of();        }

        @Override
        public Set<Def> visitExprList(ExprList exprList) {
            return Set.of();        }

        @Override
        public Set<Def> visitElseIf(ElseIf elseIf) {
            return Set.of();        }

        @Override
        public Set<Def> visitIfStmt(IfStmt ifStmt) {
            return Set.of();
        }

        @Override
        public Set<Def> visitAssignStmt(AssignStmt assignStmt) {
            return Set.of(new Def(assignStmt.getIdent(), "put it here"));
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
            // TODO: Revisit this
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
