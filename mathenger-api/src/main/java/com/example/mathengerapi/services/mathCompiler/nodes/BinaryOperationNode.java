package com.example.mathengerapi.services.mathCompiler.nodes;

import com.example.mathengerapi.services.mathCompiler.LatexNode;
import com.example.mathengerapi.services.mathCompiler.Token;
import lombok.AllArgsConstructor;
import lombok.Data;

import static com.example.mathengerapi.services.mathCompiler.TokenType.*;

@Data
@AllArgsConstructor
public class BinaryOperationNode implements LatexNode {
    private static final NodeType type = NodeType.BINARY_OPERATION;

    private LatexNode left;
    private LatexNode right;
    private Token token;
    @Override
    public void fillWithLatex(StringBuilder stringBuilder) {
        if (token.getType().equals(PLUS)) {
            left.fillWithLatex(stringBuilder);
            stringBuilder.append("+ ");
            right.fillWithLatex(stringBuilder);
        }

        if (token.getType().equals(MINUS)) {
            left.fillWithLatex(stringBuilder);
            stringBuilder.append("- ");
            right.fillWithLatex(stringBuilder);
        }

        if (token.getType().equals(MUL)) {
            left.fillWithLatex(stringBuilder);
            stringBuilder.append("\\cdot ");
            right.fillWithLatex(stringBuilder);
        }

        if (token.getType().equals(DIV)) {
            stringBuilder.append("\\frac{");
            fillWithLatexSkipParenthesis(left, stringBuilder);
            stringBuilder.append("}{");
            fillWithLatexSkipParenthesis(right, stringBuilder);
            stringBuilder.append("} ");
        }

        if (token.getType().equals(EQUALS)) {
            left.fillWithLatex(stringBuilder);
            stringBuilder.append("= ");
            right.fillWithLatex(stringBuilder);
        }

        if (token.getType().equals(NOT_EQUALS)) {
            left.fillWithLatex(stringBuilder);
            stringBuilder.append("\\neq ");
            right.fillWithLatex(stringBuilder);
        }

        if (token.getType().equals(MORE)) {
            left.fillWithLatex(stringBuilder);
            stringBuilder.append("> ");
            right.fillWithLatex(stringBuilder);
        }

        if (token.getType().equals(LESS)) {
            left.fillWithLatex(stringBuilder);
            stringBuilder.append("< ");
            right.fillWithLatex(stringBuilder);
        }

        if (token.getType().equals(MORE_EQUALS)) {
            left.fillWithLatex(stringBuilder);
            stringBuilder.append("\\geq ");
            right.fillWithLatex(stringBuilder);
        }

        if (token.getType().equals(LESS_EQUALS)) {
            left.fillWithLatex(stringBuilder);
            stringBuilder.append("\\leq ");
            right.fillWithLatex(stringBuilder);
        }

        if (token.getType().equals(EVAL_TO_DEGREE)) {
            left.fillWithLatex(stringBuilder);
            stringBuilder.append("^{");
            fillWithLatexSkipParenthesis(right, stringBuilder);
            stringBuilder.append("} ");
        }
    }

    @Override
    public NodeType getType() {
        return type;
    }

    private void fillWithLatexSkipParenthesis(LatexNode node, StringBuilder stringBuilder) {
        if (node.getType().equals(NodeType.PARENTHESIS)) {
            var parenthesisNode = (ParenthesisNode) node;
            parenthesisNode.getNestedNode().fillWithLatex(stringBuilder);
        } else {
            node.fillWithLatex(stringBuilder);
        }
    }
}
