package edu.appstate.cs.analysis.cfg;

public class Edge {
    public Node from, to;

    public Edge(Node from, Node to) {
        this.from = from;
        this.to = to;
    }

    public Edge reverse() {
        return new Edge(to, from);
    }
}