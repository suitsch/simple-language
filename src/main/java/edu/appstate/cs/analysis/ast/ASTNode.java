package edu.appstate.cs.analysis.ast;

import edu.appstate.cs.analysis.visitor.AnalysisVisitor;

/**
 * Abstract base class for all AST nodes.
 *
 * @author Mark Hills
 * @version 1.0
 */
public class ASTNode extends beaver.Symbol {
    public <R> R accept(AnalysisVisitor<R> analysisVisitor) {
        return null; // This should never be called
    }
}
