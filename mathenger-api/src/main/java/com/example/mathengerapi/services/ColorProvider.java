package com.example.mathengerapi.services;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class ColorProvider {
    private final String[] colors = {
            "#ed37b9", "#17c2bf", "#eb8517", "#e64b1c", "#c2239f", "#49ba58"
    };
    private final Random random = new Random();
    public String getRandomColor() {
        return colors[random.nextInt(colors.length)];
    }
}
