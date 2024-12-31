package com.craftinginterpreters.lox;

class Token {
    final TokenType type;
    final String lexeme;
    final Object literal;
    final int line;
    
    Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;       // Input stream that token is derived from
        this.literal = literal;     // Java value we use to represent the token
        this.line = line;
    }

    public String toString() {
        return type + " " + lexeme + " " + literal;
    }
}
