package com.example.mathengerapi.mathCompiler;

import com.example.mathengerapi.services.mathCompiler.MathCompilerService;
import org.junit.Test;

import static com.example.mathengerapi.utils.StringUtils.assertEqualsIgnoreWhiteSpaces;

public class MathCompilerServiceSimpleExpressionsTest {
    private final MathCompilerService compilerService = new MathCompilerService();

    @Test
    public void compiles2NumbersAdditionToLatex() {
        var expr = "5+2=7";
        var expected = "5 + 2 = 7";
        var result = compilerService.toLatex(expr);
        assertEqualsIgnoreWhiteSpaces(expected, result);
    }

    @Test
    public void compiles2NumbersAdditionWithSpacesToLatex() {
        var expr = " 5  + 2 =7 ";
        var expected = "5 + 2 = 7";
        var result = compilerService.toLatex(expr);
        assertEqualsIgnoreWhiteSpaces(expected, result);
    }

    @Test
    public void compilesElevationsToDegreeToLatex() {
        var expr = "5^2+2^2=2^2+25";
        var expected = "5 ^ {2} + 2 ^ {2} = 2 ^ {2} + 25";
        var result = compilerService.toLatex(expr);
        assertEqualsIgnoreWhiteSpaces(expected, result);
    }

    @Test
    public void compilesAdditionAndSubtractionOfMultipleNumbersToLatex() {
        var expr = "10-1+6-15=0";
        var expected = "10 - 1 + 6 - 15 = 0";
        var result = compilerService.toLatex(expr);
        assertEqualsIgnoreWhiteSpaces(expected, result);
    }

    @Test
    public void compilesMultiplicationsAndDivisionsOfNumbersToLatex() {
        var expr = "5*4=40/2";
        var expected = "5 \\cdot 4 = \\frac{40}{2}";
        var result = compilerService.toLatex(expr);
        assertEqualsIgnoreWhiteSpaces(expected, result);
    }

    @Test
    public void compilesCombinationOfSimpleOperationsWithSpacesToLatex() {
        var expr = "0*2 + 50 - 10/5 = 24*2 ";
        var expected = "0 \\cdot 2 + 50 - \\frac{10}{5} = 24 \\cdot 2";
        var result = compilerService.toLatex(expr);
        assertEqualsIgnoreWhiteSpaces(expected, result);
    }

    @Test
    public void compilesLessMoreSignsToLatex() {
        var expr = "5 < 10 > 4";
        var expected = "5 < 10 > 4";
        var result = compilerService.toLatex(expr);
        assertEqualsIgnoreWhiteSpaces(expected, result);
    }

    @Test
    public void compilesLessEqualsAndMoreEqualsSignsToLatex() {
        var expr = "5 <= 10 >= 4 != 20";
        var expected = "5 \\leq 10 \\geq 4 \\neq 20";
        var result = compilerService.toLatex(expr);
        assertEqualsIgnoreWhiteSpaces(expected, result);
    }

    @Test
    public void compilesSimpleExpressionsWithVariablesToLatex() {
        var expr = "a^2 + b^2 + a*b = c^2 + b*a - 0/a";
        var expected = "a ^ {2} + b ^ {2} + a \\cdot b = c ^ {2} + b \\cdot a - \\frac{0}{a}";
        var result = compilerService.toLatex(expr);
        assertEqualsIgnoreWhiteSpaces(expected, result);
    }

    @Test
    public void compilesNestedExpressionsWithBracesToLatex() {
        var expr = "a*(b+c) = a*b + a*c";
        var expected = "a \\cdot ( b + c ) = a \\cdot b + a \\cdot c";
        var result = compilerService.toLatex(expr);
        assertEqualsIgnoreWhiteSpaces(expected, result);
    }

    @Test
    public void compilesMoreComplexNestedExpressionsToLatex() {
        var expr = "(a^(b+c) + a*b)/(a+b+c) = a^(b+c)/(a+b+c) + (a*b)/(a+b+c)";
        var expected = "\\frac{a ^ {b + c} + a \\cdot b}{a + b + c} = \\frac{a ^ {b + c}}{a + b + c} + \\frac{a \\cdot b}{a + b + c}";
        var result = compilerService.toLatex(expr);
        assertEqualsIgnoreWhiteSpaces(expected, result);
    }

}
