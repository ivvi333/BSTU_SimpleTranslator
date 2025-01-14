package org.example.inter.stmt;

import org.example.inter.expr.Expr;
import org.example.symbols.Type;

public class Else extends Stmt {
    private final Expr expr;
    private final Stmt stmt1;
    private final Stmt stmt2;

    public Else(Expr expr, Stmt stmt1, Stmt stmt2) {
        this.expr = expr;
        this.stmt1 = stmt1;
        this.stmt2 = stmt2;
        if (expr.type() != Type.BOOL) {
            expr.error("Boolean required in if!");
        }
    }

    @Override
    public void gen(int begin, int after) {
        int label1 = newLabel();
        int label2 = newLabel();
        expr.jump(0, label2);
        emitLabel(label1); stmt1.gen(label1, after);
        emit("goto L%d".formatted(after));
        emitLabel(label2); stmt2.gen(label2, after);
    }
}
