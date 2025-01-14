package org.example.lexer;

public class Real extends Token {
    private final float value;

    public Real(float value) {
        super(Tag.REAL);
        this.value = value;
    }

    @Override
    public String toString() {
        return "%f".formatted(value);
    }

    public float value() {
        return value;
    }
}
