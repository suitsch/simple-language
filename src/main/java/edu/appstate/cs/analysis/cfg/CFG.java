package edu.appstate.cs.analysis.cfg;

import edu.appstate.cs.analysis.ast.*;
import edu.appstate.cs.analysis.visitor.AnalysisVisitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class CFG {
    private Set<Node> nodes = new HashSet<>();
    private Set<Edge> edges = new HashSet<>();
    private FirstFinder firstFinder = new FirstFinder();
    private FinalFinder finalFinder = new FinalFinder();
    private NodePrinter nodePrinter = new NodePrinter();

    public void buildCFG(StmtList stmtList) {
        GraphBuilder graphBuilder = new GraphBuilder();
        graphBuilder.buildCFG(stmtList);
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
            sb.append(String.format("\"N%s\" [ label = \"%s\" ];", nodeIdMap.get(node), node.accept(nodePrinter))).append("\n");
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

        public void buildCFG(StmtList stmtList) {
            edges = stmtList.accept(this);
            nodes = new  HashSet<>();
            for (Edge edge : edges) {
                nodes.add(edge.from); nodes.add(edge.to);
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
                for (Node n :  finalOfCurrent) {
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
                for (Node n :  finalOfCurrent) {
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
                for (Node n :  finalOfCurrent) {
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
            // TODO: Add the code here to link all this up, this is the
            // most complex piece...
            return Set.of();
        }

        @Override
        public Set<Edge> visitAssignStmt(AssignStmt assignStmt) {
            Set<Edge> edges = new HashSet<>();
            edges.addAll(assignStmt.getExpr().accept(this));
            Node firstNode = assignStmt.accept(firstFinder);
            for (Node n : assignStmt.getExpr().accept(finalFinder)) {
                edges.add(new Edge(n, firstNode));
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
            Node firstRight =  plusExpr.getRight().accept(firstFinder);
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
            Node firstRight =  subExpr.getRight().accept(firstFinder);
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
            Node firstRight =  multExpr.getRight().accept(firstFinder);
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
            Node firstRight =  divExpr.getRight().accept(firstFinder);
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
            // TODO: Add needed logic here
            return Set.of();
        }

        @Override
        public Set<Edge> visitWhileStmt(WhileStmt whileStmt) {
            // TODO: Add needed logic here
            return Set.of();
        }

        @Override
        public Set<Edge> visitExprStmt(ExprStmt exprStmt) {
            Set<Edge> edges = new HashSet<>();
            edges.addAll(exprStmt.getExpr().accept(this));
            Set<Node> finalNodes =  exprStmt.getExpr().accept(finalFinder);

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
            Node firstRight =  notEqlExpr.getRight().accept(firstFinder);
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
            Node firstRight =  equalExpr.getRight().accept(firstFinder);
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
            // TODO: Add needed logic here
            return Set.of();
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
            // TODO: Add needed logic here
            return Set.of();
        }

        @Override
        public Set<Edge> visitOrExpr(OrExpr orExpr) {
            Set<Edge> edges = new HashSet<>();

            // Get the edges for the left and right operands
            edges.addAll(orExpr.getLeftExpr().accept(this));
            edges.addAll(orExpr.getRightExpr().accept(this));

            // Build edges from the final nodes on the left to the first node on the right
            Node firstRight =  orExpr.getRightExpr().accept(firstFinder);
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
            Node firstRight =  andExpr.getRight().accept(firstFinder);
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
            Node firstRight =  ltExpr.getRight().accept(firstFinder);
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
            Node firstRight =  ltEqExpr.getRight().accept(firstFinder);
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
            Node firstRight =  gtExpr.getRight().accept(firstFinder);
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
            Node firstRight =  gtEqExpr.getRight().accept(firstFinder);
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
            // TODO: Add logic here
            return Set.of();
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
