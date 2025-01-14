package org.example.inter.expr;

import org.example.lexer.Word;
import org.example.symbols.Type;

public class Id extends Expr {
    private final int offset;

    public Id(Word id, Type type, int offset) {
        super(id, type);
        this.offset = offset;
    }
}
