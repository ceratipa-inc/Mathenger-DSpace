package com.example.mathengerapi.services.mathCompiler;

import lombok.RequiredArgsConstructor;

import java.util.LinkedList;
import java.util.List;

import static com.example.mathengerapi.services.mathCompiler.TokenType.*;

@RequiredArgsConstructor
public class Lexer {
    private final char[] text;
    private int pos;

    public List<Token> tokenize() {
        List<Token> tokens = new LinkedList<>();
        pos = 0;
        Token token = nextToken();
        while (token != null) {
            tokens.add(token);
            token = nextToken();
        }
        return tokens;
    }

    private Token nextToken() {
        while (currentChar() != null) {
            var character = currentChar();

            if (Character.isWhitespace(character)) {
                skipWhitespaces();
                continue;
            }

            if (Character.isLetterOrDigit(character)) {
                return variable();
            }

            if (character == '(') {
                nextChar();
                return new Token(LPAREN, "(");
            }

            if (character == ')') {
                nextChar();
                return new Token(RPAREN, ")");
            }

            if (character == '+') {
                nextChar();
                return new Token(PLUS, "+");
            }

            if (character == '-') {
                nextChar();
                return new Token(MINUS, "-");
            }

            if (character == '*') {
                nextChar();
                return new Token(MUL, "*");
            }

            if (character == '/') {
                nextChar();
                return new Token(DIV, "/");
            }

            if (character == '^') {
                nextChar();
                return new Token(EVAL_TO_DEGREE, "^");
            }

            if (character == '_') {
                nextChar();
                return new Token(INDEX, "_");
            }

            if (character == '=') {
                nextChar();
                return new Token(EQUALS, "=");
            }

            if (character == '!' && peekChar(1) == '=') {
                nextChar();
                nextChar();
                return new Token(NOT_EQUALS, "!=");
            }

            if (character == '>' && peekChar(1) == '=') {
                nextChar();
                nextChar();
                return new Token(MORE_EQUALS, ">=");
            }

            if (character == '<' && peekChar(1) == '=') {
                nextChar();
                nextChar();
                return new Token(LESS_EQUALS, "<=");
            }

            if (character == '>') {
                nextChar();
                return new Token(MORE, ">");
            }

            if (character == '<') {
                nextChar();
                return new Token(LESS, "<");
            }
            error();
        }
        return null;
    }

    private Token variable() {
        var character = currentChar();
        var value = new StringBuilder();
        while (character != null &&
                (Character.isLetterOrDigit(character) || character == '.' || character == ',')) {
            value.append(character);
            character = this.nextChar();
        }
        return new Token(VARIABLE, value.toString());
    }

    private Character currentChar() {
        return (pos > text.length - 1) ? null : text[pos];
    }

    private Character nextChar() {
        pos += 1;
        return currentChar();
    }

    private Character peekChar(int count) {
        return (pos + count > text.length - 1) ? null : text[pos + count];
    }

    private void skipWhitespaces() {
        Character character = currentChar();
        while (character != null && Character.isWhitespace(character)) {
            character = this.nextChar();
        }
    }

    private void error() {
        throw new IllegalArgumentException(
                String.format("Invalid character '%s' at position %d, ", currentChar(), pos)
        );
    }
}
