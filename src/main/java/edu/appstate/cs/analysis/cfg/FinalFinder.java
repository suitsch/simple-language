package edu.appstate.cs.analysis.cfg;

import edu.appstate.cs.analysis.ast.*;
import edu.appstate.cs.analysis.visitor.AnalysisVisitor;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class FinalFinder implements AnalysisVisitor<Set<Node>>
{
    @Override
    public Set<Node> visitStmtList(StmtList stmtList) {
        // The final step of a statement list is the final step of the final statement
        Iterator<Stmt> stmtIterator = stmtList.iterator();
        Stmt lastStmt = stmtIterator.next();
        while (stmtIterator.hasNext()) { lastStmt = stmtIterator.next(); }
        return lastStmt.accept(this);
    }

    @Override
    public Set<Node> visitElseIfList(ElseIfList elseIfList) {
        // We return the final step from each else-if as the final step from an else-if list
        Set<Node> nodes = new HashSet<>();
        for (ElseIf ei : elseIfList) {
            nodes.addAll(ei.accept(this));
        }
        return nodes;
    }

    @Override
    public Set<Node> visitExprList(ExprList exprList) {
        // The final step of an expression list is the final step of the final expression
        Iterator<Expr> exprIterator = exprList.iterator();
        Expr lastExpr = exprIterator.next();
        while (exprIterator.hasNext()) { lastExpr = exprIterator.next(); }
        return lastExpr.accept(this);
    }

    @Override
    public Set<Node> visitElseIf(ElseIf elseIf) {
        return elseIf.getBody().accept(this);
    }

    @Override
    public Set<Node> visitIfStmt(IfStmt ifStmt) {
        Set<Node> result = new HashSet<>();

        // The final item in the true branch is one possible final node
        result.addAll(ifStmt.getThenBody().accept(this));

        // The final item in each else-if branch is also a possible final node
        if (ifStmt.getElseIfs() != null && ifStmt.getElseIfs().size() > 0) {
            for (ElseIf elseIf : ifStmt.getElseIfs()) {
                result.addAll(elseIf.getBody().accept(this));
            }
        }

        // The final item in the else is also a possible final node
        if (ifStmt.getElseBody() != null && ifStmt.getElseBody().size() > 0) {
            result.addAll(ifStmt.getElseBody().accept(this));
        } else {
            // The condition is a final node if we don't have an else or any else-ifs
            if (ifStmt.getElseIfs().size() == 0) {
                result.add(new Node.ExprNode(ifStmt.getCondition()));
            }
        }

        return result;
    }

    @Override
    public Set<Node> visitAssignStmt(AssignStmt assignStmt) {
        return Set.of(new Node.StmtNode(assignStmt));
    }

    @Override
    public Set<Node> visitIntLiteral(IntLiteral intLiteral) {
        return Set.of(new Node.ExprNode(intLiteral));
    }

    @Override
    public Set<Node> visitBooleanLiteral(BooleanLiteral booleanLiteral) {
        return Set.of(new  Node.ExprNode(booleanLiteral));
    }

    @Override
    public Set<Node> visitPlusExpr(PlusExpr plusExpr) {
        return Set.of(new Node.ExprNode(plusExpr));
    }

    @Override
    public Set<Node> visitSubExpr(SubExpr subExpr) {
        return Set.of(new Node.ExprNode(subExpr));
    }

    @Override
    public Set<Node> visitMultExpr(MultExpr multExpr) {
        return Set.of(new Node.ExprNode(multExpr));
    }

    @Override
    public Set<Node> visitDivExpr(DivExpr divExpr) {
        return Set.of(new Node.ExprNode(divExpr));
    }

    @Override
    public Set<Node> visitIdentExpr(IdentExpr identExpr) {
        return Set.of(new Node.ExprNode(identExpr));
    }

    @Override
    public Set<Node> visitForStmt(ForStmt forStmt) {
        return forStmt.getStmts().accept(this);
    }

    @Override
    public Set<Node> visitWhileStmt(WhileStmt whileStmt) {
        return Set.of(new Node.ExprNode(whileStmt.getExpr()));
    }

    @Override
    public Set<Node> visitExprStmt(ExprStmt exprStmt) {
        return Set.of(new Node.StmtNode(exprStmt));
    }

    @Override
    public Set<Node> visitNotEqlExpr(NotEqlExpr notEqlExpr) {
        return Set.of(new Node.ExprNode(notEqlExpr));
    }

    @Override
    public Set<Node> visitEqualExpr(EqualExpr equalExpr) {
        return Set.of(new Node.ExprNode(equalExpr));
    }

    @Override
    public Set<Node> visitDeclStmt(DeclStmt declStmt) {
        return Set.of(new Node.StmtNode(declStmt));
    }

    @Override
    public Set<Node> visitNotExpr(NotExpr notExpr) {
        return Set.of(new Node.ExprNode(notExpr));
    }

    @Override
    public Set<Node> visitReturnStmt(ReturnStmt returnStmt) {
        return Set.of(new Node.StmtNode(returnStmt));
    }

    @Override
    public Set<Node> visitOrExpr(OrExpr orExpr) {
        return Set.of(new Node.ExprNode(orExpr));
    }

    @Override
    public Set<Node> visitAndExpr(AndExpr andExpr) {
        return Set.of(new  Node.ExprNode(andExpr));
    }

    @Override
    public Set<Node> visitLtExpr(LtExpr ltExpr) {
        return Set.of(new  Node.ExprNode(ltExpr));
    }

    @Override
    public Set<Node> visitLtEqExpr(LtEqExpr ltEqExpr) {
        return Set.of(new Node.ExprNode(ltEqExpr));
    }

    @Override
    public Set<Node> visitGtExpr(GtExpr gtExpr) {
        return Set.of(new Node.ExprNode(gtExpr));
    }

    @Override
    public Set<Node> visitGtEqExpr(GtEqExpr gtEqExpr) {
        return Set.of(new  Node.ExprNode(gtEqExpr));
    }

    @Override
    public Set<Node> visitListExpr(ListExpr listExpr) {
        return Set.of(new  Node.ExprNode(listExpr));
    }

    @Override
    public Set<Node> visitNegExpr(NegExpr negExpr) {
        return Set.of(new Node.ExprNode(negExpr));
    }
}
