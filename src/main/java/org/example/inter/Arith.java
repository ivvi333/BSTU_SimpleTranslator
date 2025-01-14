package org.example.inter;

import org.example.lexer.Token;
import org.example.symbols.Type;

public class Arith extends Op {
    private final Expr expr1;
    private final Expr expr2;

    public Arith(Token token, Expr expr1, Expr expr2) {
        super(token, null);
        this.expr1 = expr1;
        this.expr2 = expr2;
        this.type = Type.max(expr1.type, expr2.type);
        if (type == null) {
            error("Type error!");
        }
    }

    @Override
    public String toString() {
        return "%s %s %s".formatted(expr1, token, expr2);
    }

    @Override
    public Expr gen() {
        return new Arith(token, expr1.reduce(), expr2.reduce());
    }
}
