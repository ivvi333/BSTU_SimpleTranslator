package org.example.symbols;

import org.example.lexer.Tag;

public class Array extends Type {
    private final Type type;
    private final int size;

    @Override
    public String toString() {
        return "[%d]".formatted(size) + type;
    }

    public Array(int size, Type type) {
        super(Tag.INDEX, "[]", size * type.width());
        this.type = type;
        this.size = size;
    }
}
