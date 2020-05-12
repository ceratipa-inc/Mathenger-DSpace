package com.example.mathengerapi.mathCompiler;

import com.example.mathengerapi.services.mathCompiler.MathCompilerService;
import org.junit.Test;

import static com.example.mathengerapi.utils.StringUtils.assertEqualsIgnoreWhiteSpaces;

public class MathCompilerServiceInnerLatexTest {
    private final MathCompilerService compilerService = new MathCompilerService();

    @Test
    public void compilesFullLatexWithoutChanges() {
        var expr = "$\\alpha + \\beta = \\gamma + \\delta$";
        var expected = "\\alpha + \\beta = \\gamma + \\delta";
        var result = compilerService.toLatex(expr);
        assertEqualsIgnoreWhiteSpaces(expected, result);
    }

    @Test
    public void compilesPartialLatexWithoutChanges() {
        var expr = "a_i + b_j = $\\gamma + \\delta$";
        var expected = "a_{i} + b_{j} = \\gamma + \\delta";
        var result = compilerService.toLatex(expr);
        assertEqualsIgnoreWhiteSpaces(expected, result);
    }

    @Test
    public void compilesMultipleInnerLatexInsertionsInsteadOfVariables() {
        var expr = "$\\alpha$ / 2 = 4 * $\\beta$/(-$\\gamma \\cdot \\delta$)";
        var expected = "\\frac{\\alpha}{2} = \\frac{4 \\cdot \\beta}{-\\gamma \\cdot \\delta}";
        var result = compilerService.toLatex(expr);
        assertEqualsIgnoreWhiteSpaces(expected, result);
    }

    @Test
    public void compilesInnerLatexInsteadOfBinaryOperation() {
        var expr = "5 $\\cdot$ 2 = 10";
        var expected = "5 \\cdot 2 = 10";
        var result = compilerService.toLatex(expr);
        assertEqualsIgnoreWhiteSpaces(expected, result);
    }

    @Test
    public void compilesMultipleInnerLatexInsertionsInsteadOfBinaryOperation() {
        var expr = "5 $\\cdot$ 2 $\\sim$ 9 ";
        var expected = "5 \\cdot 2 \\sim 9";
        var result = compilerService.toLatex(expr);
        assertEqualsIgnoreWhiteSpaces(expected, result);
    }

}
