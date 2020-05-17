package com.example.mathengerapi.services.mathCompiler;

import org.springframework.stereotype.Service;

@Service
public class MathCompilerService {
    public String toLatex(String mathExpr) {
        var lexer = new Lexer(mathExpr.toCharArray());
        var parser = new LatexParser(lexer);
        var result = new StringBuilder();
        parser.parse().fillWithLatex(result);
        return result.toString();
    }
}
