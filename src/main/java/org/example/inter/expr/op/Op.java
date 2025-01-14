package org.example.inter.expr.op;

import org.example.inter.expr.Expr;
import org.example.inter.expr.Temp;
import org.example.lexer.Token;
import org.example.symbols.Type;

public class Op extends Expr {
    public Op(Token token, Type type) {
        super(token, type);
    }

    @Override
    public Expr reduce() {
        Expr expr = gen();
        Temp temp = new Temp(this.type());
        emit("%s = %s".formatted(temp, expr));
        return temp;
    }
}
