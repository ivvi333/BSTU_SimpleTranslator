package org.example.inter.expr.logical;

import org.example.inter.expr.Expr;
import org.example.lexer.Token;

public class And extends Logical {
    public And(Token token, Expr expr1, Expr expr2) {
        super(token, expr1, expr2);
    }

    @Override
    public void jump(int labelTrue, int labelFalse) {
        int label = labelFalse != 0 ? labelFalse : newLabel();
        expr1.jump(0, label);
        expr2.jump(labelTrue, labelFalse);
        if (labelFalse == 0) {
            emitLabel(label);
        }
    }
}
