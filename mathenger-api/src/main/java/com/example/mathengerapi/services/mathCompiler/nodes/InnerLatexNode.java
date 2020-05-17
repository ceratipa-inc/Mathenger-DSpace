package com.example.mathengerapi.services.mathCompiler.nodes;

import com.example.mathengerapi.services.mathCompiler.LatexNode;
import com.example.mathengerapi.services.mathCompiler.LatexParser;
import com.example.mathengerapi.services.mathCompiler.Lexer;
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
        String[] parts = token.getValue().split("#");
        for (int i = 0; i < parts.length; i++) {
            if (i % 2 == 0) {
                stringBuilder.append(parts[i]);
            } else {
                var lexer = new Lexer(parts[i].toCharArray());
                var parser = new LatexParser(lexer);
                parser.parse().fillWithLatex(stringBuilder);
            }
        }
    }

    @Override
    public NodeType getType() {
        return type;
    }
}
