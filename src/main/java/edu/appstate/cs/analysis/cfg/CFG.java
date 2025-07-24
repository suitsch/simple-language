package edu.appstate.cs.analysis.cfg;

import edu.appstate.cs.analysis.ast.*;
import edu.appstate.cs.analysis.visitor.AnalysisVisitor;

import java.util.*;
import java.util.stream.Collectors;

public class CFG {
    private Set<Node> nodes = new HashSet<>();
    private Set<Edge> edges = new HashSet<>();
    private FirstFinder firstFinder = new FirstFinder();
    private FinalFinder finalFinder = new FinalFinder();
    private NodePrinter nodePrinter = new NodePrinter();
    private Node.EntryNode entryNode = new Node.EntryNode();
    private Node.ExitNode exitNode = new Node.ExitNode();

    /**
     * Given a program, build the control flow graph
     *
     * @param stmtList the list of statements making up the program
     */
    public void buildCFG(StmtList stmtList) {
        GraphBuilder graphBuilder = new GraphBuilder();
        graphBuilder.buildCFG(stmtList);
    }

    /**
     * Given an input node, get the predecessors of the node in the CFG
     *
     * @param node the input node
     *
     * @return the predecessors of the input node, i.e., pred(n)
     */
    public Set<Node> getPredecessors(Node node) {
        return edges.stream()
                    .filter((e) -> e.to.equals(node))
                    .map((e) -> e.from)
                    .collect(Collectors.toSet());
    }

    /**
     * Given an input node, get the successors of the node in the CFG
     *
     * @param node the input node
     *
     * @return the successors of the input node, i.e., succ(n)
     */
    public Set<Node> getSuccessors(Node node) {
        return edges.stream()
                .filter((e) -> e.from.equals(node))
                .map((e) -> e.to)
                .collect(Collectors.toSet());
    }

    /**
     * Get the set of nodes for the graph
     *
     * @return the set of nodes
     */
    public Set<Node> getNodes() {
        return Set.copyOf(nodes);
    }

    /**
     * Get the set of edges for the graph
     *
     * @return the graph edges
     */
    public Set<Edge> getEdges() {
        return Set.copyOf(edges);
    }

    /**
     * Get the entry node for the CFG.
     *
     * @return the entry node
     */
    public Node getEntryNode() {
        return entryNode;
    }

    /**
     * Get the exit node for the CFG.
     *
     * @return the exit node
     */
    public Node getExitNode() {
        return exitNode;
    }

    public String toDot() {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph CFG {").append("\n");
        sb.append("graph [ label=\"Control Flow Graph\" ];").append("\n");
        sb.append("node [shape=box];").append("\n");

        // Assign IDs to the nodes
        HashMap<Node, String> nodeIdMap = new HashMap<>();
        int idCounter = 0;
        for (Node node : nodes) {
            nodeIdMap.put(node, Integer.toString(idCounter++));
        }

        // Print the nodes
        for (Node node : nodes) {
            if (node instanceof Node.EntryNode) {
                sb.append(String.format("\"N%s\" [ label = \"%s\" ];", nodeIdMap.get(node), "Entry")).append("\n");
            } else if (node instanceof Node.ExitNode) {
                sb.append(String.format("\"N%s\" [ label = \"%s\" ];", nodeIdMap.get(node), "Exit")).append("\n");
            } else {
                sb.append(String.format("\"N%s\" [ label = \"%s\" ];", nodeIdMap.get(node), node.accept(nodePrinter))).append("\n");
            }
        }

        // Print the edges
        for (Edge edge : edges) {
            sb.append(String.format("\"N%s\" -> \"N%s\"", nodeIdMap.get(edge.from), nodeIdMap.get(edge.to))).append("\n");
        }

        // Finalize the DOT text
        sb.append("}").append("\n");
        return sb.toString();
    }

    private class GraphBuilder implements AnalysisVisitor<Set<Edge>> {

        // TODO: Conditions with multiple else-ifs followed by an else are not linked correctly

        public void buildCFG(StmtList stmtList) {
            edges = stmtList.accept(this);

            List<Stmt> stmts = new ArrayList<>();
            for (Stmt stmt : stmtList) {
                stmts.add(stmt);
            }

            // Link the entry node to the start of the list
            Node firstNode = stmts.get(0).accept(firstFinder);
            edges.add(new Edge(entryNode, firstNode));

            // Link the exit node to the end of the list
            Set<Node> lastNodes = stmts.get(stmts.size() - 1).accept(finalFinder);
            for (Node lastNode : lastNodes) {
                edges.add(new Edge(lastNode, exitNode));
            }

            // Remove edges from return statements that don't go to the exit node
            Set<Edge> edgesToRemove = new HashSet<>();
            for (Edge e : edges) {
                if (e.from instanceof Node.StmtNode) {
                    Node.StmtNode sn = (Node.StmtNode) e.from;
                    if (sn.getStmt() instanceof ReturnStmt) {
                        if (!(e.to instanceof Node.ExitNode)) {
                            edgesToRemove.add(e);
                        }
                    }
                }
            }
            edges.removeAll(edgesToRemove);

            nodes = new HashSet<>();
            for (Edge edge : edges) {
                nodes.add(edge.from);
                nodes.add(edge.to);
            }
        }

        @Override
        public Set<Edge> visitStmtList(StmtList stmtList) {
            // We can put the resulting edges here
            Set<Edge> edges = new HashSet<>();

            // Grab the statements so we can more easily work with them
            ArrayList<Stmt> stmts = new ArrayList<>();
            for (Stmt stmt : stmtList) {
                stmts.add(stmt);
            }

            // Now, build all internal edges for individual statements
            for (Stmt stmt : stmts) {
                edges.addAll(stmt.accept(this));
            }

            // And now, link up the statements in the list
            for (int i = 0; i < (stmts.size() - 1); ++i) {
                Set<Node> finalOfCurrent = stmts.get(i).accept(finalFinder);
                Node firstOfNext = stmts.get(i + 1).accept(firstFinder);
                for (Node n : finalOfCurrent) {
                    edges.add(new Edge(n, firstOfNext));
                }
            }

            return edges;
        }

        @Override
        public Set<Edge> visitElseIfList(ElseIfList elseIfList) {
            // We can put the resulting edges here
            Set<Edge> edges = new HashSet<>();

            // Grab out the else-ifs to make them easier to work with
            ArrayList<ElseIf> elseIfs = new ArrayList<>();
            for (ElseIf elseIf : elseIfList) {
                elseIfs.add(elseIf);
            }

            // Now, visit the else-if to build all the edges there for the
            // condition and body
            for (ElseIf elseIf : elseIfs) {
                edges.addAll(elseIf.accept(this));
            }

            // Finally, link up the conditions. This represents the case where we try a
            // condition, it fails, and we go to the next condition.
            for (int i = 0; i < (elseIfs.size() - 1); ++i) {
                Set<Node> finalOfCurrent = elseIfs.get(i).getCondition().accept(finalFinder);
                Node firstOfNext = elseIfs.get(i + 1).getCondition().accept(firstFinder);
                for (Node n : finalOfCurrent) {
                    edges.add(new Edge(n, firstOfNext));
                }
            }

            return edges;
        }

        @Override
        public Set<Edge> visitExprList(ExprList exprList) {
            // We can put the resulting edges here
            Set<Edge> edges = new HashSet<>();

            // Grab out the expressions to make them easier to work with
            ArrayList<Expr> exprs = new ArrayList<>();
            for (Expr expr : exprList) {
                exprs.add(expr);
            }

            // Build all the internal edges for the expressions
            for (Expr expr : exprs) {
                edges.addAll(expr.accept(this));
            }

            // Finally, link up the expressions
            for (int i = 0; i < (exprs.size() - 1); ++i) {
                Set<Node> finalOfCurrent = exprs.get(i).accept(finalFinder);
                Node firstOfNext = exprs.get(i + 1).accept(firstFinder);
                for (Node n : finalOfCurrent) {
                    edges.add(new Edge(n, firstOfNext));
                }
            }

            return edges;
        }

        @Override
        public Set<Edge> visitElseIf(ElseIf elseIf) {
            Set<Edge> edges = new HashSet<>();
            edges.addAll(elseIf.getCondition().accept(this));
            edges.addAll(elseIf.getBody().accept(this));
            Node firstInBody = elseIf.getBody().accept(firstFinder);
            Set<Node> lastInCondition = elseIf.getCondition().accept(finalFinder);
            for (Node n : lastInCondition) {
                edges.add(new Edge(n, firstInBody));
            }
            return edges;
        }

        @Override
        public Set<Edge> visitIfStmt(IfStmt ifStmt) {
            Set<Edge> edges = new HashSet<>();

            // Compute all the internal edges
            edges.addAll(ifStmt.getCondition().accept(this));
            edges.addAll(ifStmt.getThenBody().accept(this));
            if (ifStmt.getElseIfs() != null) {
                edges.addAll(ifStmt.getElseIfs().accept(this));
            }
            if (ifStmt.getElseBody() != null) {
                edges.addAll(ifStmt.getElseBody().accept(this));
            }

            // Link the condition properly -- it goes to the then body, but
            // also to the first else-if (if present) or else (if present).
            // Note that we don't have labels on our edges -- if we did, we
            // would include information on the condition there.
            Set<Node> endOfCondition = ifStmt.getCondition().accept(finalFinder);
            Node firstOfThen = ifStmt.getThenBody().accept(firstFinder);
            for (Node n : endOfCondition) {
                edges.add(new Edge(n, firstOfThen));
            }

            if (ifStmt.getElseIfs() != null) {
                Node firstOfElseIfs = ifStmt.getElseIfs().accept(firstFinder);
                for (Node n : endOfCondition) {
                    edges.add(new Edge(n, firstOfElseIfs));
                }
            } else if (ifStmt.getElseBody() != null) {
                Node firstOfElseBody = ifStmt.getElseBody().accept(firstFinder);
                for (Node n : endOfCondition) {
                    edges.add(new Edge(n, firstOfElseBody));
                }
            }

            return edges;
        }

        @Override
        public Set<Edge> visitAssignStmt(AssignStmt assignStmt) {
            Set<Edge> edges = new HashSet<>();
            edges.addAll(assignStmt.getExpr().accept(this));
            Node assignNode = new Node.StmtNode(assignStmt);
            for (Node n : assignStmt.getExpr().accept(finalFinder)) {
                edges.add(new Edge(n, assignNode));
            }
            return edges;
        }

        @Override
        public Set<Edge> visitIntLiteral(IntLiteral intLiteral) {
            return Set.of();
        }

        @Override
        public Set<Edge> visitBooleanLiteral(BooleanLiteral booleanLiteral) {
            return Set.of();
        }

        @Override
        public Set<Edge> visitPlusExpr(PlusExpr plusExpr) {
            Set<Edge> edges = new HashSet<>();

            // Get the edges for the left and right operands
            edges.addAll(plusExpr.getLeft().accept(this));
            edges.addAll(plusExpr.getRight().accept(this));

            // Build edges from the final nodes on the left to the first node on the right
            Node firstRight = plusExpr.getRight().accept(firstFinder);
            Set<Node> finalLeft = plusExpr.getLeft().accept(finalFinder);
            for (Node n : finalLeft) {
                edges.add(new Edge(n, firstRight));
            }

            // Build edges from the final nodes on the right to the overall expression
            for (Node n : plusExpr.getRight().accept(finalFinder)) {
                for (Node o : plusExpr.accept(finalFinder)) {
                    edges.add(new Edge(n, o));
                }
            }
            return edges;
        }

        @Override
        public Set<Edge> visitSubExpr(SubExpr subExpr) {
            Set<Edge> edges = new HashSet<>();

            // Get the edges for the left and right operands
            edges.addAll(subExpr.getLeft().accept(this));
            edges.addAll(subExpr.getRight().accept(this));

            // Build edges from the final nodes on the left to the first node on the right
            Node firstRight = subExpr.getRight().accept(firstFinder);
            Set<Node> finalLeft = subExpr.getLeft().accept(finalFinder);
            for (Node n : finalLeft) {
                edges.add(new Edge(n, firstRight));
            }

            // Build edges from the final nodes on the right to the overall expression
            for (Node n : subExpr.getRight().accept(finalFinder)) {
                for (Node o : subExpr.accept(finalFinder)) {
                    edges.add(new Edge(n, o));
                }
            }
            return edges;
        }

        @Override
        public Set<Edge> visitMultExpr(MultExpr multExpr) {
            Set<Edge> edges = new HashSet<>();

            // Get the edges for the left and right operands
            edges.addAll(multExpr.getLeft().accept(this));
            edges.addAll(multExpr.getRight().accept(this));

            // Build edges from the final nodes on the left to the first node on the right
            Node firstRight = multExpr.getRight().accept(firstFinder);
            Set<Node> finalLeft = multExpr.getLeft().accept(finalFinder);
            for (Node n : finalLeft) {
                edges.add(new Edge(n, firstRight));
            }

            // Build edges from the final nodes on the right to the overall expression
            // Build edges from the final nodes on the right to the overall expression
            for (Node n : multExpr.getRight().accept(finalFinder)) {
                for (Node o : multExpr.accept(finalFinder)) {
                    edges.add(new Edge(n, o));
                }
            }
            return edges;
        }

        @Override
        public Set<Edge> visitDivExpr(DivExpr divExpr) {
            Set<Edge> edges = new HashSet<>();

            // Get the edges for the left and right operands
            edges.addAll(divExpr.getLeft().accept(this));
            edges.addAll(divExpr.getRight().accept(this));

            // Build edges from the final nodes on the left to the first node on the right
            Node firstRight = divExpr.getRight().accept(firstFinder);
            Set<Node> finalLeft = divExpr.getLeft().accept(finalFinder);
            for (Node n : finalLeft) {
                edges.add(new Edge(n, firstRight));
            }

            // Build edges from the final nodes on the right to the overall expression
            for (Node n : divExpr.getRight().accept(finalFinder)) {
                for (Node o : divExpr.accept(finalFinder)) {
                    edges.add(new Edge(n, o));
                }
            }
            return edges;
        }

        @Override
        public Set<Edge> visitIdentExpr(IdentExpr identExpr) {
            return Set.of();
        }

        @Override
        public Set<Edge> visitForStmt(ForStmt forStmt) {
            Set<Edge> edges = new HashSet<>();

            // Add the edges for the expression and the body
            edges.addAll(forStmt.getExpr().accept(this));
            edges.addAll(forStmt.getStmts().accept(this));

            // Judgement call: the expression should only be evaluated once,
            // to get the list to iterate over, so we link from that but then
            // we loop back to the first statement, NOT to the expression.
            Set<Node> exprEndNodes = forStmt.getExpr().accept(finalFinder);
            Node firstStmtNode = forStmt.getStmts().accept(firstFinder);
            Set<Node> finalBodyNodes = forStmt.getStmts().accept(finalFinder);

            // First, from the expression to the body
            for (Node en : exprEndNodes) {
                edges.add(new Edge(en, firstStmtNode));
            }

            // Then the backedge at the end, back to the start of the body
            for (Node fbn : finalBodyNodes) {
                edges.add(new Edge(fbn, firstStmtNode));
            }

            // NOTE: We do NOT represent the assignment each iteration into the loop
            // variable. We would need a synthetic node to represent this.
            return edges;
        }

        @Override
        public Set<Edge> visitWhileStmt(WhileStmt whileStmt) {
            Set<Edge> edges = new HashSet<>();

            // Add edges from the condition and body
            edges.addAll(whileStmt.getExpr().accept(this));
            edges.addAll(whileStmt.getStmts().accept(this));

            // Link the condition to the body
            Set<Node> finalConditionNodes = whileStmt.getExpr().accept(finalFinder);
            Node firstBodyNode = whileStmt.getStmts().accept(firstFinder);
            for (Node n : finalConditionNodes) {
                edges.add(new Edge(n, firstBodyNode));
            }

            // Link the end of the body back to the condition
            Set<Node> finalBodyNodes = whileStmt.getStmts().accept(finalFinder);
            Node firstConditionNode = whileStmt.getExpr().accept(firstFinder);
            for (Node n : finalBodyNodes) {
                edges.add(new Edge(n, firstConditionNode));
            }

            return edges;
        }

        @Override
        public Set<Edge> visitExprStmt(ExprStmt exprStmt) {
            Set<Edge> edges = new HashSet<>();
            edges.addAll(exprStmt.getExpr().accept(this));
            Set<Node> finalNodes = exprStmt.getExpr().accept(finalFinder);

            // Build edges from the final nodes on the right to the overall expression
            for (Node n : finalNodes) {
                for (Node o : exprStmt.accept(finalFinder)) {
                    edges.add(new Edge(n, o));
                }
            }
            return edges;
        }

        @Override
        public Set<Edge> visitNotEqlExpr(NotEqlExpr notEqlExpr) {
            Set<Edge> edges = new HashSet<>();

            // Get the edges for the left and right operands
            edges.addAll(notEqlExpr.getLeft().accept(this));
            edges.addAll(notEqlExpr.getRight().accept(this));

            // Build edges from the final nodes on the left to the first node on the right
            Node firstRight = notEqlExpr.getRight().accept(firstFinder);
            Set<Node> finalLeft = notEqlExpr.getLeft().accept(finalFinder);
            for (Node n : finalLeft) {
                edges.add(new Edge(n, firstRight));
            }

            // Build edges from the final nodes on the right to the overall expression
            for (Node n : notEqlExpr.getRight().accept(finalFinder)) {
                for (Node o : notEqlExpr.accept(finalFinder)) {
                    edges.add(new Edge(n, o));
                }
            }
            return edges;
        }

        @Override
        public Set<Edge> visitEqualExpr(EqualExpr equalExpr) {
            Set<Edge> edges = new HashSet<>();

            // Get the edges for the left and right operands
            edges.addAll(equalExpr.getLeft().accept(this));
            edges.addAll(equalExpr.getRight().accept(this));

            // Build edges from the final nodes on the left to the first node on the right
            Node firstRight = equalExpr.getRight().accept(firstFinder);
            Set<Node> finalLeft = equalExpr.getLeft().accept(finalFinder);
            for (Node n : finalLeft) {
                edges.add(new Edge(n, firstRight));
            }

            // Build edges from the final nodes on the right to the overall expression
            for (Node n : equalExpr.getRight().accept(finalFinder)) {
                for (Node o : equalExpr.accept(finalFinder)) {
                    edges.add(new Edge(n, o));
                }
            }
            return edges;
        }

        @Override
        public Set<Edge> visitDeclStmt(DeclStmt declStmt) {
            Set<Edge> edges = new HashSet<>();

            if (declStmt.getExpr() != null) {
                edges.addAll(declStmt.getExpr().accept(this));
                Set<Node> finalNodes = declStmt.getExpr().accept(finalFinder);
                Set<Node> declNodes = declStmt.accept(finalFinder);
                for (Node n : finalNodes) {
                    for (Node m : declNodes) {
                        edges.add(new Edge(n, m));
                    }
                }
            }

            return edges;
        }

        @Override
        public Set<Edge> visitNotExpr(NotExpr notExpr) {
            Set<Edge> edges = new HashSet<>();
            edges.addAll(notExpr.getExpr().accept(this));

            // Build edges from the final nodes on the right to the overall expression
            for (Node n : notExpr.getExpr().accept(finalFinder)) {
                for (Node o : notExpr.accept(finalFinder)) {
                    edges.add(new Edge(n, o));
                }
            }
            return edges;
        }

        @Override
        public Set<Edge> visitReturnStmt(ReturnStmt returnStmt) {
            Set<Edge> edges = new HashSet<>();
            edges.addAll(returnStmt.getRetExpr().accept(this));

            // Build edges from the final nodes on the right to the overall expression
            Node returnNode = new Node.StmtNode(returnStmt);
            for (Node n : returnStmt.getRetExpr().accept(finalFinder)) {
                edges.add(new Edge(n, returnNode));
            }

            // Add an edge from the return node to the exit
            edges.add(new Edge(returnNode, exitNode));

            return edges;
        }

        @Override
        public Set<Edge> visitOrExpr(OrExpr orExpr) {
            Set<Edge> edges = new HashSet<>();

            // Get the edges for the left and right operands
            edges.addAll(orExpr.getLeftExpr().accept(this));
            edges.addAll(orExpr.getRightExpr().accept(this));

            // Build edges from the final nodes on the left to the first node on the right
            Node firstRight = orExpr.getRightExpr().accept(firstFinder);
            Set<Node> finalLeft = orExpr.getLeftExpr().accept(finalFinder);
            for (Node n : finalLeft) {
                edges.add(new Edge(n, firstRight));
            }

            // Build edges from the final nodes on the right to the overall expression
            for (Node n : orExpr.getRightExpr().accept(finalFinder)) {
                for (Node o : orExpr.accept(finalFinder)) {
                    edges.add(new Edge(n, o));
                }
            }
            return edges;

        }

        @Override
        public Set<Edge> visitAndExpr(AndExpr andExpr) {
            Set<Edge> edges = new HashSet<>();

            // Get the edges for the left and right operands
            edges.addAll(andExpr.getLeft().accept(this));
            edges.addAll(andExpr.getRight().accept(this));

            // Build edges from the final nodes on the left to the first node on the right
            Node firstRight = andExpr.getRight().accept(firstFinder);
            Set<Node> finalLeft = andExpr.getLeft().accept(finalFinder);
            for (Node n : finalLeft) {
                edges.add(new Edge(n, firstRight));
            }

            // Build edges from the final nodes on the right to the overall expression
            for (Node n : andExpr.getRight().accept(finalFinder)) {
                for (Node o : andExpr.accept(finalFinder)) {
                    edges.add(new Edge(n, o));
                }
            }
            return edges;
        }

        @Override
        public Set<Edge> visitLtExpr(LtExpr ltExpr) {
            Set<Edge> edges = new HashSet<>();

            // Get the edges for the left and right operands
            edges.addAll(ltExpr.getLeft().accept(this));
            edges.addAll(ltExpr.getRight().accept(this));

            // Build edges from the final nodes on the left to the first node on the right
            Node firstRight = ltExpr.getRight().accept(firstFinder);
            Set<Node> finalLeft = ltExpr.getLeft().accept(finalFinder);
            for (Node n : finalLeft) {
                edges.add(new Edge(n, firstRight));
            }

            // Build edges from the final nodes on the right to the overall expression
            for (Node n : ltExpr.getRight().accept(finalFinder)) {
                for (Node o : ltExpr.accept(finalFinder)) {
                    edges.add(new Edge(n, o));
                }
            }
            return edges;
        }

        @Override
        public Set<Edge> visitLtEqExpr(LtEqExpr ltEqExpr) {
            Set<Edge> edges = new HashSet<>();

            // Get the edges for the left and right operands
            edges.addAll(ltEqExpr.getLeft().accept(this));
            edges.addAll(ltEqExpr.getRight().accept(this));

            // Build edges from the final nodes on the left to the first node on the right
            Node firstRight = ltEqExpr.getRight().accept(firstFinder);
            Set<Node> finalLeft = ltEqExpr.getLeft().accept(finalFinder);
            for (Node n : finalLeft) {
                edges.add(new Edge(n, firstRight));
            }

            // Build edges from the final nodes on the right to the overall expression
            for (Node n : ltEqExpr.getRight().accept(finalFinder)) {
                for (Node o : ltEqExpr.accept(finalFinder)) {
                    edges.add(new Edge(n, o));
                }
            }
            return edges;
        }

        @Override
        public Set<Edge> visitGtExpr(GtExpr gtExpr) {
            Set<Edge> edges = new HashSet<>();

            // Get the edges for the left and right operands
            edges.addAll(gtExpr.getLeft().accept(this));
            edges.addAll(gtExpr.getRight().accept(this));

            // Build edges from the final nodes on the left to the first node on the right
            Node firstRight = gtExpr.getRight().accept(firstFinder);
            Set<Node> finalLeft = gtExpr.getLeft().accept(finalFinder);
            for (Node n : finalLeft) {
                edges.add(new Edge(n, firstRight));
            }

            // Build edges from the final nodes on the right to the overall expression
            for (Node n : gtExpr.getRight().accept(finalFinder)) {
                for (Node o : gtExpr.accept(finalFinder)) {
                    edges.add(new Edge(n, o));
                }
            }
            return edges;
        }

        @Override
        public Set<Edge> visitGtEqExpr(GtEqExpr gtEqExpr) {
            Set<Edge> edges = new HashSet<>();

            // Get the edges for the left and right operands
            edges.addAll(gtEqExpr.getLeft().accept(this));
            edges.addAll(gtEqExpr.getRight().accept(this));

            // Build edges from the final nodes on the left to the first node on the right
            Node firstRight = gtEqExpr.getRight().accept(firstFinder);
            Set<Node> finalLeft = gtEqExpr.getLeft().accept(finalFinder);
            for (Node n : finalLeft) {
                edges.add(new Edge(n, firstRight));
            }

            // Build edges from the final nodes on the right to the overall expression
            for (Node n : gtEqExpr.getRight().accept(finalFinder)) {
                for (Node o : gtEqExpr.accept(finalFinder)) {
                    edges.add(new Edge(n, o));
                }
            }
            return edges;
        }

        @Override
        public Set<Edge> visitListExpr(ListExpr listExpr) {
            Set<Edge> edges = new HashSet<>();
            edges.addAll(listExpr.getList().accept(this));

            Set<Node> finalNodes = listExpr.getList().accept(finalFinder);
            Node listNode = new Node.ExprNode(listExpr);
            for (Node n : finalNodes) {
                edges.add(new Edge(n, listNode));
            }

            return edges;
        }

        @Override
        public Set<Edge> visitNegExpr(NegExpr negExpr) {
            Set<Edge> edges = new HashSet<>();
            edges.addAll(negExpr.getExpr().accept(this));

            // Build edges from the final nodes on the right to the overall expression
            for (Node n : negExpr.getExpr().accept(finalFinder)) {
                for (Node o : negExpr.accept(finalFinder)) {
                    edges.add(new Edge(n, o));
                }
            }
            return edges;
        }
    }
}
