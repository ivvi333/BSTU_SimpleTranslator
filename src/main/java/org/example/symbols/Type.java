package org.example.symbols;

import org.example.lexer.Tag;
import org.example.lexer.Word;

public class Type extends Word {
    private final int width;

    public Type(int tag, String lexeme, int width) {
        super(tag, lexeme);
        this.width = width;
    }

    public static boolean numeric(Type type) {
        return type == Type.CHAR || type == Type.INT || type == Type.FLOAT;
    }

    public static Type max(Type type1, Type type2) {
        if (!numeric(type1) || !numeric(type2)) return null;
        else if (type1 == Type.FLOAT || type2 == Type.FLOAT) return Type.FLOAT;
        else if (type1 == Type.INT || type2 == Type.INT) return Type.INT;
        else return Type.CHAR;
    }

    public static final Type
            INT = new Type(Tag.BASIC, "int", 4),
            FLOAT = new Type(Tag.BASIC, "float", 8),
            CHAR = new Type(Tag.BASIC, "char", 1),
            BOOL = new Type(Tag.BASIC, "bool", 1);

    public int width() {
        return width;
    }
}
