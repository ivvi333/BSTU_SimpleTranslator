package org.example.inter.stmt;

import org.example.inter.expr.Expr;
import org.example.inter.expr.Id;
import org.example.symbols.Type;

public class Set extends Stmt {
    private final Id id;
    private final Expr expr;

    public Set(Id id, Expr expr) {
        this.id = id;
        this.expr = expr;
        if (check(id.type(), expr.type()) == null) {
            expr.error("Type error!");
        }
    }

    @Override
    public void gen(int begin, int after) {
        emit("%s = %s".formatted(id, expr.gen()));
    }

    private Type check(Type type1, Type type2) {
        if (Type.numeric(type1) && Type.numeric(type2)) {
            return type2;
        }
        else if (type1 == Type.BOOL && type2 == Type.BOOL) {
            return type2;
        }
        else {
            return null;
        }
    }
}
