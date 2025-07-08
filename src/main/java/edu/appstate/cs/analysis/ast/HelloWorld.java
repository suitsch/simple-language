package edu.appstate.cs.analysis.ast;

public class HelloWorld extends ASTNode {
    public HelloWorld() {
        System.out.println("Building a HelloWorld node");
    }

    @Override
    public String toString() {
        return "Hello World!";
    }
}
