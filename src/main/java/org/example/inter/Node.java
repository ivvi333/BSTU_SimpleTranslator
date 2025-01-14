package org.example.inter;

import org.example.lexer.Lexer;

import java.io.PrintStream;

public class Node {
    // Выходной поток для результатов трансляции
    public static final PrintStream printStream = System.out;
    private final int sourceLine;
    private static int labelCount = 0;

    public Node() {
        this.sourceLine = Lexer.line();
    }

    public int newLabel() {
        return ++labelCount;
    }

    public void emitLabel(int label) {
        printStream.printf("L%d:", label);
    }

    public void emit(String line) {
        printStream.printf("\t%s%n", line);
    }

    protected void error(String message) {
        throw new RuntimeException("near line %d: %s".formatted(sourceLine, message));
    }
}
