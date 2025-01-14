package org.example.inter.expr.op;

import org.example.inter.expr.Expr;
import org.example.lexer.Token;
import org.example.symbols.Type;

public class Unary extends Op {
    private final Expr expr;

    public Unary(Token token, Expr expr) {
        super(token, null);
        this.expr = expr;
        this.setType(Type.max(Type.INT, expr.type()));
        if (this.type() == null) {
            error("Type error!");
        }
    }

    @Override
    public String toString() {
        return "%s %s".formatted(this.token(), expr);
    }

    @Override
    public Expr gen() {
        return new Unary(this.token(), expr.reduce());
    }
}
