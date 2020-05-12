package com.example.mathengerapi.services.mathCompiler.nodes;

import com.example.mathengerapi.services.mathCompiler.LatexNode;
import com.example.mathengerapi.services.mathCompiler.Token;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InnerLatexNode implements LatexNode {
    private static final NodeType type = NodeType.INNER_LATEX;
    private Token token;

    @Override
    public void fillWithLatex(StringBuilder stringBuilder) {
        stringBuilder.append(token.getValue()).append(" ");
    }

    @Override
    public NodeType getType() {
        return type;
    }
}
