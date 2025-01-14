package org.example.inter.stmt;

import org.example.inter.expr.Expr;
import org.example.symbols.Type;

public class If extends Stmt {
    private final Expr expr;
    private final Stmt stmt;

    public If(Expr expr, Stmt stmt) {
        this.expr = expr;
        this.stmt = stmt;
        if (expr.type() != Type.BOOL) {
            expr.error("Boolean required in if!");
        }
    }

    @Override
    public void gen(int begin, int after) {
        int label = newLabel();
        expr.jump(0, after);
        emitLabel(label);
        stmt.gen(label, after);
    }
}
