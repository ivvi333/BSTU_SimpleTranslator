package org.example.inter;

import org.example.lexer.Token;
import org.example.symbols.Array;
import org.example.symbols.Type;

public class Rel extends Logical {
    public Rel(Token token, Expr expr1, Expr expr2) {
        super(token, expr1, expr2);
    }

    @Override
    public void jump(int labelTrue, int labelFalse) {
        Expr a = expr1.reduce();
        Expr b = expr2.reduce();
        emitJump("%s %s %s".formatted(a, token, b), labelTrue, labelFalse);
    }

    private Type check(Type type1, Type type2) {
        if (type1 instanceof Array || type2 instanceof Array) return null;
        else if (type1 == type2) return Type.BOOL;
        else return null;
    }
}
