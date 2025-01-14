package org.example.lexer;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Lexer {
    private final InputStream inputStream;
    private int line = 1;
    private char peek = ' ';
    private final Map<String, Word> words = new HashMap<>(Map.ofEntries(
            Map.entry(Word.IF.lexeme(), Word.IF),
            Map.entry(Word.ELSE.lexeme(), Word.ELSE),
            Map.entry(Word.WHILE.lexeme(), Word.WHILE),
            Map.entry(Word.DO.lexeme(), Word.DO),
            Map.entry(Word.BREAK.lexeme(), Word.BREAK),
            Map.entry(Word.TRUE.lexeme(), Word.TRUE),
            Map.entry(Word.FALSE.lexeme(), Word.FALSE)
    ));

    public Lexer(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    private void readChar() throws IOException {
        peek = (char) inputStream.read();
    }

    private boolean readChar(char c) throws IOException {
        readChar();
        if (peek != c) {
            return false;
        }
        peek = ' ';
        return true;
    }

    public Token scan() throws IOException {
        // Пропуск пробельных символов
        for (;;readChar()) {
            if (peek == ' ' || peek == '\t') { continue; }
            else if (peek == '\n') { line++; }
            else break;
        }
        // Распознаём составные токены
        switch (peek) {
            case '&' -> {
                if (readChar('&')) return Word.AND;
                else return new Token('&');
            }
            case '|' -> {
                if (readChar('|')) return Word.OR;
                else return new Token('|');
            }
            case '=' -> {
                if (readChar('=')) return Word.EQ;
                else return new Token('=');
            }
            case '!' -> {
                if (readChar('=')) return Word.NE;
                else return new Token('!');
            }
            case '>' -> {
                if (readChar('=')) return Word.GE;
                else return new Token('>');
            }
            case '<' -> {
                if (readChar('=')) return Word.LE;
                else return new Token('<');
            }
        }
        // Распознаём числа
        if (Character.isDigit(peek)) {
            int value = 0;
            do {
                value = value * 10 + Character.digit(peek, 10);
                readChar();
            } while(Character.isDigit(peek));
            // Если не нашли точку - целое число
            if (peek != '.') return new Num(value);
            float floatValue = value;
            float denominator = 10;
            for (;;) {
                readChar();
                if (!Character.isDigit(peek)) break;
                floatValue = floatValue + Character.digit(peek, 10) / denominator;
                denominator *= 10;
            }
            return new Real(floatValue);
        }
        // Распознаём идентификаторы и ключевые слова
        if (Character.isLetter(peek)) {
            StringBuilder stringBuilder = new StringBuilder();
            do {
                stringBuilder.append(peek);
                readChar();
            } while(Character.isLetterOrDigit(peek));
            String lexeme = stringBuilder.toString();
            Word word = words.get(lexeme);
            // Если не нашли слово среди зарезервированных - идентификатор
            if (word == null) {
                word = new Word(Tag.ID, lexeme);
                words.put(lexeme, word);
            }
            return word;
        }
        // Если не смогли распознать конкретный токен, выполняем приведение через конструктор
        Token token = new Token(peek);
        peek = ' ';
        return token;
    }

    public int line() {
        return line;
    }

    public char peek() {
        return peek;
    }

    public Map<String, Word> words() {
        return words;
    }
}
