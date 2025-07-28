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
        // Step 1: Compute gen, we need a visitor for this!
        Map<String, Set<Def>> gen = new HashMap<>(); // A map from node IDs to the set of defs gen'ed by the node
        GenVisitor genVisitor = new GenVisitor();
        for (Node n : cfg.getNodes()) {
            Set<Def> defsForNode = n.accept(genVisitor);
            if (defsForNode == null) {
                defsForNode = Collections.emptySet();
            }
            gen.put(cfg.getNodeId(n), defsForNode);
        }

        // NOTE: This is informational, we would normally remove this...
        System.out.println("Gen: " + gen);

        // Step 2: Compute defs, this can be based on gen
        Map<String, Set<Def>> defs = new HashMap<>(); // A map from variable name to the set of defs of that name
        for (String nodeId : gen.keySet()) {
            for (Def def : gen.get(nodeId)) {
                if (! defs.containsKey(def.name)) {
                    defs.put(def.name, new HashSet<>());
                }
                defs.get(def.name).add(def);
            }
        }

        // NOTE: This is informational, we would normally remove this...
        System.out.println("Defs: " + defs);

        // Step 3: Compute kill based on gen and defs
        Map<String, Set<Def>> kills = new HashMap<>(); // A map from node IDs to the set of defs killed by that node
        for (String nodeId : gen.keySet()) {
            // Store all the defs killed at this node
            Set<Def> nodeKills = new HashSet<>();

            // If we generated anything in this node, use that to compute kills
            if (gen.get(nodeId).size() > 0) {
                for (Def def : gen.get(nodeId)) {
                    // Given a definition of a name n, we kill all other definitions of n
                    Set<Def> killSet = new HashSet<>(defs.get(def.name));
                    killSet.remove(def);
                    nodeKills.addAll(killSet);
                }
            }
            kills.put(nodeId, nodeKills);
        }

        // NOTE: This is informational, we would normally remove this...
        System.out.println("Kills: " + kills);

        // Step 4: Use this info to compute reach. We will do this
        // and iterate until the info stabilizes.
        Map<String, Set<Def>> reachIn = new HashMap<>();
        Map<String, Set<Def>> reachOut = new HashMap<>();
        for (Node n : cfg.getNodes()) {
            reachIn.put(cfg.getNodeId(n), Collections.emptySet());
            reachOut.put(cfg.getNodeId(n), Collections.emptySet());
        }
        
        Map<String, Set<Def>> oldReachIn = Collections.emptyMap();
        Map<String, Set<Def>> oldReachOut = Collections.emptyMap();
        do {
            // We keep going while reachIn and reachOut change, so we need to save
            // their current state here.
            oldReachIn = new HashMap<>(reachIn);
            oldReachOut = new HashMap<>(reachOut);

            // TODO: Using the algorithm for reaching defs, iterate over all
            // nodes and update the reach sets. This is what you need to update
            // for the lab.
            for (Node n : cfg.getNodes()) {
                String nLabel = cfg.getNodeId(n);
                Set<Node> preds = cfg.getPredecessors(n);
                Set<Def> defsFromPredecessors = new HashSet<>();
                for (Node p : preds) {
                    defsFromPredecessors.addAll(oldReachOut.get(cfg.getNodeId(p)));
                }
                reachIn.put(nLabel, defsFromPredecessors);

                // TODO: Compute reach out!
            }

        } while (!reachIn.equals(oldReachIn) || !reachOut.equals(oldReachOut));

        return reachIn; // This shows which defs reach each node, so we return this
    }

    // What goes here???
    private class GenVisitor implements AnalysisVisitor<Set<Def>> {

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
            return Set.of(new Def(assignStmt.getIdent(), cfg.getNodeId(new Node.StmtNode(assignStmt))));
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
