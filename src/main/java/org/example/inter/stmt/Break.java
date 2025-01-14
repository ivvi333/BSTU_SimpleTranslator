package org.example.inter.stmt;

public class Break extends Stmt {
    private final Stmt stmt;

    public Break() {
        if (Stmt.enclosing == null) {
            error("Unenclosed break!");
        }
        this.stmt = Stmt.enclosing;
    }

    @Override
    public void gen(int begin, int after) {
        emit("goto L%d".formatted(stmt.saved));
    }
}
