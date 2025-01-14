package org.example.inter.expr.logical;

import org.example.inter.expr.Expr;
import org.example.inter.expr.Temp;
import org.example.lexer.Token;
import org.example.symbols.Type;

public class Logical extends Expr {
    protected final Expr expr1;
    protected final Expr expr2;

    public Logical(Token token, Expr expr1, Expr expr2) {
        super(token, null);
        this.expr1 = expr1;
        this.expr2 = expr2;
        this.setType(check(expr1.type(), expr2.type()));
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
        int f = newLabel(); int a = newLabel();
        Temp temp = new Temp(this.type());
        jump(0, f);
        emit("%s = true".formatted(temp));
        emit("goto L%d".formatted(a));
        emitLabel(f);
        emit("%s = false".formatted(temp));
        emitLabel(a);
        return temp;
    }

    private Type check(Type type1, Type type2) {
        if (type1 == Type.BOOL && type2 == Type.BOOL) return Type.BOOL;
        else return null;
    }
}
