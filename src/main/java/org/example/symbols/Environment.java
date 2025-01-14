package org.example.symbols;

import org.example.inter.expr.Id;
import org.example.lexer.Token;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    private final Map<Token, Id> table = new HashMap<>();
    protected final Environment previous;

    public Environment(Environment previous) {
        this.previous = previous;
    }

    public void put(Token token, Id id) {
        table.put(token, id);
    }

    public Id get(Token token) {
        for (Environment environment = this; environment != null; environment = environment.previous) {
            Id found = environment.table.get(token);
            if (found != null) return found;
        }
        return null;
    }
}
