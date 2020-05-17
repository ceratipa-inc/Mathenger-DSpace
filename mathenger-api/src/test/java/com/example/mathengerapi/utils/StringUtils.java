package com.example.mathengerapi.utils;

import org.junit.Assert;

public class StringUtils {
    public static void assertEqualsIgnoreWhiteSpaces(String expected, String result) {
        Assert.assertEquals(expected.replace(" ", ""),
                result.replace(" ", ""));
    }
}
