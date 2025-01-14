package org.example.inter;

import org.example.lexer.Token;
import org.example.symbols.Type;

public class Id extends Expr {
    private final int offset;

    public Id(int sourceLine, Token token, Type type, int offset) {
        super(sourceLine, token, type);
        this.offset = offset;
    }
}
