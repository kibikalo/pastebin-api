package com.kibikalo.pastebin.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PasteNameGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int NAME_LENGTH = 24;

    public String generateRandomName() {
        StringBuilder sb = new StringBuilder(NAME_LENGTH);
        Random random = new Random();

        for (int i = 0; i < NAME_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }
}
