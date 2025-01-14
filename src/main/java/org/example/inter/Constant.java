package org.example.inter;

import org.example.lexer.Num;
import org.example.lexer.Token;
import org.example.lexer.Word;
import org.example.symbols.Type;

public class Constant extends Expr {
    public Constant(Token token, Type type) {
        super(token, type);
    }

    public Constant(int value) {
        super(new Num(value), Type.INT);
    }

    @Override
    public void jump(int labelTrue, int labelFalse) {
        if (this == TRUE && labelTrue != 0) {
            emit("goto L%d".formatted(labelTrue));
        }
        else if (this == FALSE && labelFalse != 0) {
            emit("goto L%d".formatted(labelFalse));
        }
    }

    public static final Constant
            TRUE = new Constant(Word.TRUE, Type.BOOL),
            FALSE = new Constant(Word.FALSE, Type.BOOL);
}
