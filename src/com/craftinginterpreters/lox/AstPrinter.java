package com.craftinginterpreters.lox;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

class AstPrinter implements Expr.Visitor<String>, Stmt.Visitor<String> {
    String print(Stmt stmt) {
        return stmt.accept(this);
    }

    String print(List<Stmt> statements) {
        String ret = "";
        for(Stmt s: statements){
            ret += print(s) + "\n";
        }
        return ret;
    }

    String stringify(List<Token> tokens) {
        String ret = "";
        for(Token t: tokens){
            ret += t.lexeme;
        }
        return ret;
    }

    @Override
    public String visitReturnStmt(Stmt.Return stmt) {
        return "ret";
    }

    @Override
    public String visitFunctionStmt(Stmt.Function stmt) {
        String ret = "<fun ";
        ret += "(" + stmt.name.lexeme + ", " + stringify(stmt.params) + ", "
                + print(stmt.body) + ")>";
        return ret;
    }

    @Override
    public String visitBlockStmt(Stmt.Block stmt) {
        String ret = "{";
        for (Stmt s : stmt.statements) {
            ret += print(s);
        }
        ret += "}";

        return ret;
    }

    @Override
    public String visitExpressionStmt(Stmt.Expression stmt) {
        return print(stmt.expression);
    }

    @Override
    public String visitIfStmt(Stmt.If stmt) {
        String ret = "if (" + print(stmt.condition) + ") {";
        ret += print(stmt.thenBranch) + "}";

        if (stmt.elseBranch != null) {
            ret += "\nelse {" + print(stmt.thenBranch) + "}";
        }

        return ret;
    }

    @Override
    public String visitWhileStmt(Stmt.While stmt) {
        String ret = "while (" + print(stmt.condition) + ") + { " +
                print(stmt.body) + " }";
        return ret;
    }

    @Override
    public String visitPrintStmt(Stmt.Print stmt) {
        return "print " + print(stmt.expression);
    }

    @Override
    public String visitVarStmt(Stmt.Var stmt) {
        String ret = stmt.name.lexeme + " = ";
        if (stmt.initializer != null) {
            ret += print(stmt.initializer);
        }
        return ret;
    }

    String print(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitAssignExpr(Expr.Assign expr) {
        return expr.name.lexeme;
    }

    @Override
    public String visitLogicalExpr(Expr.Logical expr) {
        return parenthesize(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return parenthesize(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return parenthesize("group", expr.expression);
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if (expr.value == null)
            return "nil";
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return parenthesize(expr.operator.lexeme, expr.right);
    }

    @Override
    public String visitVariableExpr(Expr.Variable var) {
        return var.name.lexeme;
    }

    @Override
    public String visitCallExpr(Expr.Call expr) {
        // TODO: update this to something more sensible
        return expr.callee.toString();
    }

    private String parenthesize(String name, Expr ...exprs) {
        StringBuilder builder = new StringBuilder();

        builder.append("(").append(name);
        for (Expr expr : exprs) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");

        return builder.toString();
    }

    public static void main(String[] args) throws IOException {
        String path = args[0];
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        String source = new String(bytes, Charset.defaultCharset());
        try {
            Scanner scanner = new Scanner(source);
            List<Token> tokens = scanner.scanTokens();
            Parser parser = new Parser(tokens);
            List<Stmt> statements = parser.parse();
            System.out.println(new AstPrinter().print(statements));

        } catch(Exception e) {
            System.err.println("error: " + e.getMessage());
        }

    }
}
