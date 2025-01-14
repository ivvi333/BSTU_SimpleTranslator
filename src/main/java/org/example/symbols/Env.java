package org.example.symbols;

import org.example.inter.Id;
import org.example.lexer.Token;

import java.util.HashMap;
import java.util.Map;

public class Env {
    private final Map<Token, Id> table = new HashMap<>();
    protected final Env previous;

    public Env(Env previous) {
        this.previous = previous;
    }

    public void put(Token token, Id id) {
        table.put(token, id);
    }

    public Id get(Token token) {
        for (Env env = this; env != null; env = env.previous) {
            Id found = env.table.get(token);
            if (found != null) return found;
        }
        return null;
    }
}
