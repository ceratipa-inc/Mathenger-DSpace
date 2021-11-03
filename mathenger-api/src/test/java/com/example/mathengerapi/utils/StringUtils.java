package com.example.mathengerapi.utils;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class StringUtils {
    public static void assertEqualsIgnoreWhiteSpaces(String expected, String result) {
        assertThat(expected.replace(" ", ""))
                .isEqualTo(result.replace(" ", ""));
    }
}
