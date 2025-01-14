package org.example.inter.stmt;

public class Seq extends Stmt {
    private final Stmt stmt1;
    private final Stmt stmt2;

    public Seq(Stmt stmt1, Stmt stmt2) {
        this.stmt1 = stmt1;
        this.stmt2 = stmt2;
    }

    @Override
    public void gen(int begin, int after) {
        if (stmt1 == Stmt.NULL) {
            stmt2.gen(begin, after);
        }
        else if (stmt2 == Stmt.NULL) {
            stmt1.gen(begin, after);
        }
        else {
            int label = newLabel();
            stmt1.gen(begin, label);
            emitLabel(label);
            stmt2.gen(label, after);
        }
    }
}
