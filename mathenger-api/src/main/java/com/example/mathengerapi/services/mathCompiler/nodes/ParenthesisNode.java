package com.example.mathengerapi.services.mathCompiler.nodes;

import com.example.mathengerapi.services.mathCompiler.LatexNode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class ParenthesisNode implements LatexNode {
    private static final NodeType type = NodeType.PARENTHESIS;
    @Getter
    private LatexNode nestedNode;

    @Override
    public void fillWithLatex(StringBuilder stringBuilder) {
        stringBuilder.append("( ");
        nestedNode.fillWithLatex(stringBuilder);
        stringBuilder.append(") ");
    }

    @Override
    public NodeType getType() {
        return type;
    }
}
