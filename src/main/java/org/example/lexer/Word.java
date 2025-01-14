package org.example.lexer;

public class Word extends Token {
    private final String lexeme;

    public Word(int tag, String lexeme) {
        super(tag);
        this.lexeme = lexeme;
    }

    @Override
    public String toString() {
        return lexeme;
    }

    public static final Word
            AND = new Word(Tag.AND, "&&"),
            OR = new Word(Tag.OR, "||") ,
            EQ = new Word(Tag.EQ, "=="),
            NE = new Word(Tag.NE, "!="),
            GE = new Word(Tag.GE, ">="),
            LE = new Word(Tag.LE, "<="),
            TRUE = new Word(Tag.TRUE, "true"),
            FALSE = new Word(Tag.FALSE, "false"),
            BREAK = new Word(Tag.BREAK, "break"),
            DO = new Word(Tag.DO, "do"),
            WHILE = new Word(Tag.WHILE, "while"),
            IF = new Word(Tag.IF, "if"),
            ELSE = new Word(Tag.ELSE, "else"),
            MINUS = new Word(Tag.MINUS, "minus"),
            TEMP = new Word(Tag.TEMP, "t");

    public String lexeme() {
        return lexeme;
    }
}
