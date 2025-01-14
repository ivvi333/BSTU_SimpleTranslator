package org.example.inter;

import org.example.lexer.Token;
import org.example.symbols.Type;

public class Unary extends Op {
    private final Expr expr;

    public Unary(Token token, Expr expr) {
        super(token, null);
        this.expr = expr;
        this.type = Type.max(Type.INT, expr.type);
        if (type == null) {
            error("Type error!");
        }
    }

    @Override
    public String toString() {
        return "%s %s".formatted(token, expr);
    }

    @Override
    public Expr gen() {
        return new Unary(token, expr.reduce());
    }
}
