package org.example.inter.stmt;

import org.example.inter.Node;

public class Stmt extends Node {
    public static Stmt enclosing = Stmt.NULL;
    protected int saved = 0;

    public void gen(int begin, int after) {
    }

    public static final Stmt NULL = new Stmt();
}
