package com.example.mathengerapi.services.mathCompiler;

import com.example.mathengerapi.services.mathCompiler.nodes.BinaryOperationNode;
import com.example.mathengerapi.services.mathCompiler.nodes.ParenthesisNode;
import com.example.mathengerapi.services.mathCompiler.nodes.UnaryOperationNode;
import com.example.mathengerapi.services.mathCompiler.nodes.VariableNode;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

import static com.example.mathengerapi.services.mathCompiler.TokenType.*;

@RequiredArgsConstructor
public class LatexParser {
    private final Lexer lexer;
    private List<Token> tokens;
    private int pos;

    public LatexNode parse() {
        tokens = lexer.tokenize();
        pos = 0;
        return expr();
    }

    private LatexNode element() {
        // element : (UNARY OPERATOR) element | VARIABLE | LPAREN expr RPAREN
        var token = currentToken();

        if (token == null) {
            return null;
        }

        if (token.getType().equals(VARIABLE)) {
            eat(VARIABLE);
            return new VariableNode(token);
        }

        if (token.getType().equals(LPAREN)) {
            eat(LPAREN);
            var node = expr();
            eat(RPAREN);
            return new ParenthesisNode(node);
        }

        if (Arrays.asList(PLUS, MINUS).contains(token.getType())) {
            eat(token.getType());
            var node = element();
            return new UnaryOperationNode(token, node);
        }

        error();
        return null;
    }

    private LatexNode expr() {
        // expr : 1stPrExpr (BINARY_OPERATION 1stPrExpr)*

        if (currentToken() == null) {
            return null;
        }

        var lowPriorityBinaryTokenTypes = Arrays.asList(PLUS, MINUS, EQUALS,
                NOT_EQUALS, MORE, LESS, MORE_EQUALS, LESS_EQUALS);

        var node = firstPriorityExpr();

        while (currentToken() != null && lowPriorityBinaryTokenTypes.contains(currentToken().getType())) {
            var token = currentToken();
            eat(token.getType());
            node = new BinaryOperationNode(node, firstPriorityExpr(), token);
        }

        return node;
    }

    private LatexNode firstPriorityExpr() {
        // 1stPrExpr : element (1ST_PRIORITY_BINARY_OPERATION element)*

        if (currentToken() == null) {
            return null;
        }

        var firstPriorityBinaryTokenTypes = Arrays.asList(MUL, DIV, EVAL_TO_DEGREE);

        var node = element();

        while (currentToken() != null &&
                firstPriorityBinaryTokenTypes.contains(currentToken().getType())) {
            var token = currentToken();
            eat(token.getType());
            node = new BinaryOperationNode(node, element(), token);
        }

        return node;
    }

    private Token currentToken() {
        return (pos > tokens.size() - 1) ? null : tokens.get(pos);
    }

    private Token peekToken(int count) {
        return (pos + count > tokens.size() - 1 || pos + count < 0) ? null : tokens.get(pos + count);
    }

    private void eat(TokenType tokenType) {
        if (tokenType.equals(currentToken().getType())) {
            pos += 1;
        } else {
            error();
        }
    }

    private void error() {
        throw new IllegalArgumentException("Invalid syntax!");
    }
}
