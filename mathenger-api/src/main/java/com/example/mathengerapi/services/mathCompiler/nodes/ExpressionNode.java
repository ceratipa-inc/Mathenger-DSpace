package com.example.mathengerapi.services.mathCompiler.nodes;

import com.example.mathengerapi.services.mathCompiler.LatexNode;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

@Data
@AllArgsConstructor
public class ExpressionNode implements LatexNode {
    private static final NodeType type = NodeType.EXPRESSION;
    private Collection<LatexNode> innerNodes;

    @Override
    public void fillWithLatex(StringBuilder stringBuilder) {
        innerNodes.forEach(node -> node.fillWithLatex(stringBuilder));
    }

    @Override
    public NodeType getType() {
        return type;
    }
}
