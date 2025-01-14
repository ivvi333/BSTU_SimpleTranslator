package org.example.inter.expr.op;

import org.example.inter.expr.Expr;
import org.example.lexer.Token;
import org.example.symbols.Type;

public class Arith extends Op {
    private final Expr expr1;
    private final Expr expr2;

    public Arith(Token token, Expr expr1, Expr expr2) {
        super(token, null);
        this.expr1 = expr1;
        this.expr2 = expr2;
        this.setType(Type.max(expr1.type(), expr2.type()));
        if (this.type() == null) {
            error("Type error!");
        }
    }

    @Override
    public String toString() {
        return "%s %s %s".formatted(expr1, this.token(), expr2);
    }

    @Override
    public Expr gen() {
        return new Arith(this.token(), expr1.reduce(), expr2.reduce());
    }
}
