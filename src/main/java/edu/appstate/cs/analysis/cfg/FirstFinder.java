package edu.appstate.cs.analysis.cfg;

import edu.appstate.cs.analysis.ast.*;
import edu.appstate.cs.analysis.visitor.AnalysisVisitor;

public class FirstFinder implements AnalysisVisitor<Node> {
    @Override
    public Node visitStmtList(StmtList stmtList) {
        if (stmtList.size() > 0) {
            return stmtList.iterator().next().accept(this);
        } else {
            return null;
        }
    }

    @Override
    public Node visitElseIfList(ElseIfList elseIfList) {
        if (elseIfList.size() > 0) {
            return elseIfList.iterator().next().accept(this);
        } else {
            return null;
        }
    }

    @Override
    public Node visitExprList(ExprList exprList) {
        if (exprList.size() > 0) {
            return exprList.iterator().next().accept(this);
        } else {
            return null;
        }
    }

    @Override
    public Node visitElseIf(ElseIf elseIf) {
        return elseIf.getCondition().accept(this);
    }

    @Override
    public Node visitIfStmt(IfStmt ifStmt) {
        return ifStmt.getCondition().accept(this);
    }

    @Override
    public Node visitAssignStmt(AssignStmt assignStmt) {
        return assignStmt.getExpr().accept(this);
    }

    @Override
    public Node visitIntLiteral(IntLiteral intLiteral) {
        return new Node.ExprNode(intLiteral);
    }

    @Override
    public Node visitBooleanLiteral(BooleanLiteral booleanLiteral) {
        return new Node.ExprNode(booleanLiteral);
    }

    @Override
    public Node visitPlusExpr(PlusExpr plusExpr) {
        return plusExpr.getLeft().accept(this);
    }

    @Override
    public Node visitSubExpr(SubExpr subExpr) {
        return subExpr.getLeft().accept(this);
    }

    @Override
    public Node visitMultExpr(MultExpr multExpr) {
        return multExpr.getLeft().accept(this);
    }

    @Override
    public Node visitDivExpr(DivExpr divExpr) {
        return divExpr.getLeft().accept(this);
    }

    @Override
    public Node visitIdentExpr(IdentExpr identExpr) {
        return new Node.ExprNode(identExpr);
    }

    @Override
    public Node visitForStmt(ForStmt forStmt) {
        return forStmt.getExpr().accept(this);
    }

    @Override
    public Node visitWhileStmt(WhileStmt whileStmt) {
        return whileStmt.getExpr().accept(this);
    }

    @Override
    public Node visitExprStmt(ExprStmt exprStmt) {
        return exprStmt.getExpr().accept(this);
    }

    @Override
    public Node visitNotEqlExpr(NotEqlExpr notEqlExpr) {
        return notEqlExpr.getLeft().accept(this);
    }

    @Override
    public Node visitEqualExpr(EqualExpr equalExpr) {
        return equalExpr.getLeft().accept(this);
    }

    @Override
    public Node visitDeclStmt(DeclStmt declStmt) {
        if (declStmt.getExpr() != null) {
            return declStmt.getExpr().accept(this);
        } else {
            return new Node.StmtNode(declStmt);
        }
    }

    @Override
    public Node visitNotExpr(NotExpr notExpr) {
        return notExpr.getExpr().accept(this);
    }

    @Override
    public Node visitReturnStmt(ReturnStmt returnStmt) {
        return returnStmt.getRetExpr().accept(this);
    }

    @Override
    public Node visitOrExpr(OrExpr orExpr) {
        return orExpr.getLeftExpr().accept(this);
    }

    @Override
    public Node visitAndExpr(AndExpr andExpr) {
        return andExpr.getLeft().accept(this);
    }

    @Override
    public Node visitLtExpr(LtExpr ltExpr) {
        return ltExpr.getLeft().accept(this);
    }

    @Override
    public Node visitLtEqExpr(LtEqExpr ltEqExpr) {
        return ltEqExpr.getLeft().accept(this);
    }

    @Override
    public Node visitGtExpr(GtExpr gtExpr) {
        return gtExpr.getLeft().accept(this);
    }

    @Override
    public Node visitGtEqExpr(GtEqExpr gtEqExpr) {
        return gtEqExpr.getLeft().accept(this);
    }

    @Override
    public Node visitListExpr(ListExpr listExpr) {
        if (listExpr.getList().size() > 0) {
            return listExpr.getList().accept(this);
        }
        return new Node.ExprNode(listExpr);
    }

    @Override
    public Node visitNegExpr(NegExpr negExpr) {
        return negExpr.getExpr().accept(this);
    }
}
