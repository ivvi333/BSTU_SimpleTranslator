package org.example.inter.stmt;

import org.example.inter.expr.Expr;
import org.example.symbols.Type;

public class Do extends Stmt {
    private Expr expr;
    private Stmt stmt;

    public void init(Expr expr, Stmt stmt) {
        this.expr = expr;
        this.stmt = stmt;
        if (expr.type() != Type.BOOL) {
            expr.error("Boolean required in do!");
        }
    }

    @Override
    public void gen(int begin, int after) {
        saved = after;
        int label = newLabel();
        stmt.gen(begin, label);
        emitLabel(label);
        expr.jump(begin, 0);
    }
}
