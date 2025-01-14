package org.example.inter;

import org.example.lexer.Token;
import org.example.symbols.Type;

public class Expr extends Node {
    private final Token token;
    private final Type type;

    public Expr(int sourceLine, Token token, Type type) {
        super(sourceLine);
        this.token = token;
        this.type = type;
    }

    @Override
    public String toString() {
        return token.toString();
    }

    // Возвращает выражение, которое может находиться в правой части трёхадресной команды
    public Expr gen() {
        return this;
    }

    // Возвращает свёрнутое в единственный адрес выражение - константу, идентификатор или временное имя
    public Expr reduce() {
        return this;
    }

    public void jump(int labelTrue, int labelFalse) {
        emitJump(toString(), labelTrue, labelFalse);
    }

    protected void emitJump(String condition, int labelTrue, int labelFalse) {
        if (labelTrue != 0 && labelFalse != 0) {
            emit("if %s goto L%d".formatted(condition, labelTrue));
            emit("goto L%d".formatted(labelFalse));
        }
        else if (labelTrue != 0) {
            emit("if %s goto L%d".formatted(condition, labelTrue));
        }
        else if (labelFalse != 0) {
            emit("iffalse %s goto L%d".formatted(condition, labelFalse));
        }
    }
}
