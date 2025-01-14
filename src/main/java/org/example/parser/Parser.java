package org.example.parser;

import org.example.inter.expr.Constant;
import org.example.inter.expr.Expr;
import org.example.inter.expr.Id;
import org.example.inter.expr.logical.And;
import org.example.inter.expr.logical.Not;
import org.example.inter.expr.logical.Or;
import org.example.inter.expr.logical.Rel;
import org.example.inter.expr.op.Access;
import org.example.inter.expr.op.Arith;
import org.example.inter.expr.op.Unary;
import org.example.inter.stmt.*;
import org.example.lexer.*;
import org.example.symbols.Array;
import org.example.symbols.Environment;
import org.example.symbols.Type;

import java.io.IOException;

public class Parser {
    private final Lexer lexer;
    private Token look;
    private Environment top = null;
    private int usedMemory = 0;

    public Parser(Lexer lexer) throws IOException {
        this.lexer = lexer;
        move();
    }

    private void move() throws IOException {
        look = lexer.scan();
    }

    private void error(String message) {
        throw new RuntimeException("near line %d: %s".formatted(Lexer.line(), message));
    }

    private void match(int tag) throws IOException {
        if (look.tag() == tag) move();
        else error("Syntax error!");
    }


    /**
     * program -> block
     */
    public void program() throws IOException {
        Stmt stmt = block();
        int begin = stmt.newLabel();
        int after = stmt.newLabel();
        stmt.emitLabel(begin);
        stmt.gen(begin, after);
        stmt.emitLabel(after);
    }

    /**
     * block -> { decls stmts }
     */
    private Stmt block() throws IOException {
        match('{');
        Environment saved = top;
        top = new Environment(top);
        decls();
        Stmt stmt = stmts();
        match('}');
        top = saved;
        return stmt;
    }

    /**
     * decls -> decls decl
     * |
     * <p>
     * decl -> type id;
     */
    private void decls() throws IOException {
        while (look.tag() == Tag.BASIC) {
            Type type = type();
            Token token = look;
            match(Tag.ID);
            match(';');
            Id id = new Id((Word) token, type, usedMemory);
            top.put(token, id);
            usedMemory += type.width();
        }
    }

    /**
     * type -> type [ num ] | basic
     */
    private Type type() throws IOException {
        Type type = (Type) look;
        match(Tag.BASIC);
        if (look.tag() != '[') return type;
        else return dims(type);
    }

    private Type dims(Type type) throws IOException {
        match('[');
        Token token = look;
        match(Tag.NUM);
        match(']');
        if (look.tag() == '[') {
            type = dims(type);
        }
        return new Array(((Num) token).value(), type);
    }

    /**
     * stmts -> stmts stmt
     * |
     */
    private Stmt stmts() throws IOException {
        if (look.tag() == '}') return Stmt.NULL;
        else return new Seq(stmt(), stmts());
    }

    /**
     * stmt -> if ( bool ) stmt
     * | if ( bool ) stmt else stmt
     * | while ( bool ) stmt
     * | do stmt while ( bool ) ;
     * | break ;
     * | block
     */
    private Stmt stmt() throws IOException {
        Expr expr;
        Stmt stmt1, stmt2, saved;
        return switch (look.tag()) {
            case ';' -> {
                move();
                yield Stmt.NULL;
            }
            case Tag.IF -> {
                match(Tag.IF);
                match('(');
                expr = bool();
                match(')');
                stmt1 = stmt();
                if (look.tag() != Tag.ELSE) {
                    yield new If(expr, stmt1);
                }
                match(Tag.ELSE);
                stmt2 = stmt();
                yield new Else(expr, stmt1, stmt2);
            }
            case Tag.WHILE -> {
                While whileNode = new While();
                saved = Stmt.enclosing;
                Stmt.enclosing = whileNode;
                match(Tag.WHILE);
                match('(');
                expr = bool();
                match(')');
                stmt1 = stmt();
                whileNode.init(expr, stmt1);
                Stmt.enclosing = saved;
                yield whileNode;
            }
            case Tag.DO -> {
                Do doNode = new Do();
                saved = Stmt.enclosing;
                Stmt.enclosing = doNode;
                match(Tag.DO);
                stmt1 = stmt();
                match(Tag.WHILE);
                match('(');
                expr = bool();
                match(')');
                match(';');
                doNode.init(expr, stmt1);
                Stmt.enclosing = saved;
                yield doNode;
            }
            case Tag.BREAK -> {
                match(Tag.BREAK);
                match(';');
                yield new Break();
            }
            case '{' -> block();
            default -> assign();
        };
    }

    /**
     * stmt -> loc = bool;
     * <p>
     * loc -> loc [ bool ]
     * | id
     */
    private Stmt assign() throws IOException {
        Stmt stmt;
        Token token = look;
        match(Tag.ID);
        Id id = top.get(token);
        if (id == null) {
            error("%s undeclared!".formatted(token));
        }
        if (look.tag() == '=') {
            move();
            stmt = new Set(id, bool());
        } else {
            Access access = offset(id);
            match('=');
            stmt = new SetElem(access, bool());
        }
        match(';');
        return stmt;
    }

    /**
     * bool -> bool || join
     * | join
     */
    private Expr bool() throws IOException {
        Expr expr = join();
        while (look.tag() == Tag.OR) {
            Token token = look;
            move();
            expr = new Or(token, expr, join());
        }
        return expr;
    }

    /**
     * join -> join && equality
     * | equality
     */
    private Expr join() throws IOException {
        Expr expr = equality();
        while (look.tag() == Tag.AND) {
            Token token = look;
            move();
            expr = new And(token, expr, equality());
        }
        return expr;
    }

    /**
     * equality -> equality == rel
     * | equality != rel
     * | rel
     */
    private Expr equality() throws IOException {
        Expr expr = rel();
        while (look.tag() == Tag.EQ || look.tag() == Tag.NE) {
            Token token = look;
            move();
            expr = new Rel(token, expr, rel());
        }
        return expr;
    }

    /**
     * rel -> expr <= expr
     * | expr >= expr
     * | expr < expr
     * | expr > expr
     * | expr
     */
    private Expr rel() throws IOException {
        Expr expr = expr();
        if (look.tag() == Tag.LE || look.tag() == Tag.GE
                || look.tag() == '<' || look.tag() == '>') {
            Token token = look;
            move();
            return new Rel(token, expr, expr());
        }
        return expr;
    }

    /**
     * expr -> expr + term
     * | expr - term
     * | term
     */
    private Expr expr() throws IOException {
        Expr expr = term();
        while (look.tag() == '+' || look.tag() == '-') {
            Token token = look;
            move();
            expr = new Arith(token, expr, term());
        }
        return expr;
    }

    /**
     * term -> term * unary
     * | term / unary
     * | unary
     */
    private Expr term() throws IOException {
        Expr expr = unary();
        while (look.tag() == '*' || look.tag() == '/') {
            Token token = look;
            move();
            expr = new Arith(token, expr, unary());
        }
        return expr;
    }

    /**
     * unary -> - unary
     * | ! unary
     * | factor
     */
    private Expr unary() throws IOException {
        return switch (look.tag()) {
            case '-' -> {
                move();
                yield new Unary(Word.MINUS, unary());
            }
            case '!' -> {
                Token token = look;
                move();
                yield new Not(token, unary());
            }
            default -> factor();
        };
    }

    /**
     * factor -> ( bool )
     * | loc
     * | num
     * | real
     * | true
     * | false
     */
    private Expr factor() throws IOException {
        Expr expr = null;
        return switch (look.tag()) {
            case '(' -> {
                move();
                expr = bool();
                match(')');
                yield expr;
            }
            case Tag.NUM -> {
                expr = new Constant(look, Type.INT);
                move();
                yield expr;
            }
            case Tag.REAL -> {
                expr = new Constant(look, Type.FLOAT);
                move();
                yield expr;
            }
            case Tag.TRUE -> {
                expr = Constant.TRUE;
                move();
                yield expr;
            }
            case Tag.FALSE -> {
                expr = Constant.FALSE;
                move();
                yield expr;
            }
            case Tag.ID -> {
                Id id = top.get(look);
                if (id == null) {
                    error("%s undeclared!".formatted(look));
                }
                move();
                yield (look.tag() != '[') ? id : offset(id);
            }
            default -> {
                error("Syntax error!");
                yield expr;
            }
        };
    }

    private Access offset(Id id) throws IOException {
        Expr index, width, t1, t2, loc;
        Type type = id.type();
        match('[');
        index = bool();
        match(']');
        type = ((Array) type).type();
        width = new Constant(type.width());
        t1 = new Arith(new Token('*'), index, width);
        loc = t1;
        while (look.tag() == '[') {
            match('[');
            index = bool();
            match(']');
            type = ((Array) type).type();
            width = new Constant(type.width());
            t1 = new Arith(new Token('*'), index, width);
            t2 = new Arith(new Token('+'), loc, t1);
            loc = t2;
        }
        return new Access(id, loc, type);
    }
}
