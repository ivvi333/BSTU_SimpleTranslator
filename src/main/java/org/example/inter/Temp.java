package org.example.inter;

import org.example.lexer.Word;
import org.example.symbols.Type;

public class Temp extends Expr {
    private static int count = 0;
    private final int number;

    public Temp(Type type) {
        super(Word.TEMP, type);
        number = ++count;
    }

    @Override
    public String toString() {
        return "t%d".formatted(number);
    }
}
