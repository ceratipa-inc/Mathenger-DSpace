package com.example.mathengerapi.services.mathCompiler;

import com.example.mathengerapi.services.mathCompiler.nodes.NodeType;

public interface LatexNode {
    void fillWithLatex(StringBuilder stringBuilder);
    NodeType getType();
}
