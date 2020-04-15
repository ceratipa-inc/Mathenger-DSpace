package com.example.mathengerapi.services.mathCompiler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Token {
    private final TokenType type;
    private final String value;
}
