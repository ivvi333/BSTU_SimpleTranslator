package org.example.inter.stmt;

import org.example.inter.expr.Expr;
import org.example.inter.expr.Id;
import org.example.inter.expr.op.Access;
import org.example.symbols.Array;
import org.example.symbols.Type;

public class SetElem extends Stmt {
    private final Id array;
    private final Expr index;
    private final Expr expr;

    public SetElem(Access access, Expr expr) {
        this.array = access.array();
        this.index = access.index();
        this.expr = expr;
    }

    @Override
    public void gen(int begin, int after) {
        emit("%s [ %s ] = %s".formatted(array, index.reduce(), expr.reduce()));
    }

    private Type check(Type type1, Type type2) {
        if (type1 instanceof Array || type2 instanceof Array) {
            return null;
        }
        else if (type1 == type2) {
            return type2;
        }
        else if (Type.numeric(type1) && Type.numeric(type2)) {
            return type2;
        }
        else {
            return null;
        }
    }
}
