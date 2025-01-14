package org.example.inter;

import org.example.lexer.Token;

public class Not extends Logical {
    public Not(Token token, Expr expr2) {
        super(token, expr2, expr2);
    }

    @Override
    public String toString() {
        return "%s %s".formatted(token, expr2);
    }

    @Override
    public void jump(int labelTrue, int labelFalse) {
        expr2.jump(labelFalse, labelTrue);
    }
}
