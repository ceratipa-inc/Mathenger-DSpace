package com.example.mathengerapi.services.mathCompiler.nodes;

import com.example.mathengerapi.services.mathCompiler.LatexNode;
import com.example.mathengerapi.services.mathCompiler.Token;
import com.example.mathengerapi.services.mathCompiler.TokenType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UnaryOperationNode implements LatexNode {
    private static final NodeType type = NodeType.UNARY_OPERATION;
    private Token token;
    private LatexNode nextNode;

    @Override
    public void fillWithLatex(StringBuilder stringBuilder) {
        if (token.getType().equals(TokenType.PLUS)) {
            stringBuilder.append("+ ");
        }
        if (token.getType().equals(TokenType.MINUS)) {
            stringBuilder.append("- ");
        }
        if (nextNode != null) {
            nextNode.fillWithLatex(stringBuilder);
        }
    }

    @Override
    public NodeType getType() {
        return type;
    }
}
