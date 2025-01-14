package org.example.inter;

import org.example.lexer.Token;
import org.example.symbols.Type;

public class Op extends Expr {
    public Op(Token token, Type type) {
        super(token, type);
    }

    @Override
    public Expr reduce() {
        Expr expr = gen();
        Temp temp = new Temp(type);
        emit("%s = %s".formatted(temp, expr));
        return temp;
    }
}
