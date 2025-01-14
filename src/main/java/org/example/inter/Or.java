package org.example.inter;

import org.example.lexer.Token;

public class Or extends Logical {
    public Or(Token token, Expr expr1, Expr expr2) {
        super(token, expr1, expr2);
    }

    @Override
    public void jump(int labelTrue, int labelFalse) {
        int label = labelTrue != 0 ? labelTrue : newLabel();
        expr1.jump(label, 0);
        expr2.jump(labelTrue, labelFalse);
        if (labelTrue == 0) {
            emitLabel(label);
        }
    }
}
