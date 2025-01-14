package org.example;

import org.example.lexer.Lexer;
import org.example.parser.Parser;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Lexer lexer = new Lexer(System.in);
        Parser parser = new Parser(lexer);
        parser.program();
        System.out.println();
    }
}