package org.example.inter.expr.op;

import org.example.inter.expr.Expr;
import org.example.inter.expr.Id;
import org.example.lexer.Tag;
import org.example.lexer.Word;
import org.example.symbols.Type;

public class Access extends Op {
    private final Id array;
    private final Expr index;

    public Access(Id array, Expr index, Type type) {
        super(new Word(Tag.INDEX, "[]"), type);
        this.array = array;
        this.index = index;
    }

    @Override
    public String toString() {
        return "%s[%s]".formatted(array, index);
    }

    @Override
    public Expr gen() {
        return new Access(array, index.reduce(), this.type());
    }

    @Override
    public void jump(int labelTrue, int labelFalse) {
        emitJump(reduce().toString(), labelTrue, labelFalse);
    }

    public Id array() {
        return array;
    }

    public Expr index() {
        return index;
    }
}
