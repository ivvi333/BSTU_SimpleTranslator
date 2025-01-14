package org.example.inter;

import org.example.lexer.Token;
import org.example.symbols.Type;

public class Id extends Expr {
    private final int offset;

    public Id(Token token, Type type, int offset) {
        super(token, type);
        this.offset = offset;
    }
}
