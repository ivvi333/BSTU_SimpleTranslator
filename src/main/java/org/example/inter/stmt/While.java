package org.example.inter.stmt;

import org.example.inter.expr.Expr;
import org.example.symbols.Type;

public class While extends Stmt {
    private Expr expr;
    private Stmt stmt;

    public void init(Expr expr, Stmt stmt) {
        this.expr = expr;
        this.stmt = stmt;
        if (expr.type() != Type.BOOL) {
            expr.error("Boolean required in while!");
        }
    }

    @Override
    public void gen(int begin, int after) {
        saved = after;
        expr.jump(0, after);
        int label = newLabel();
        emitLabel(label); stmt.gen(label, begin);
        emit("goto L%d".formatted(begin));
    }
}
