package org.example.lexer;

public class Token {
    private final int tag;

    public Token(int tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "%c".formatted(tag);
    }

    public int tag() {
        return tag;
    }
}
