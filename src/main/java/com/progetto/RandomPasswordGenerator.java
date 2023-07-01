package com.progetto;

import java.security.SecureRandom;

public class RandomPasswordGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=<>?/{}[]|";

    public static String generateRandomString() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        int length = random.nextInt(11) + 5; // lunghezza tra 10 and 15

        // controllo su maiuscole, caratteri speciali e numeri
        boolean hasUppercase = false;
        int numberCount = 0;
        boolean hasSpecialCharacter = false;

        while (sb.length() < length) {
            char c = CHARACTERS.charAt(random.nextInt(CHARACTERS.length()));
            if (c >= 'A' && c <= 'Z') {
                hasUppercase = true;
            } else if (c >= '0' && c <= '9') {
                numberCount++;
            } else if (!isExcludedCharacter(c)) {
                hasSpecialCharacter = true;
            }
            sb.append(c);
        }

        // sanity check per vedere se ho tutto, altrimenti aggiungi carattere
        if (!hasUppercase) {
            sb.setCharAt(random.nextInt(sb.length()), getRandomUppercase());
        }
        if (numberCount < 2) {
            for (int i = 0; i < 2 - numberCount; i++) {
                sb.setCharAt(random.nextInt(sb.length()), getRandomNumber());
            }
        }
        if (!hasSpecialCharacter) {
            sb.setCharAt(random.nextInt(sb.length()), getRandomSpecialCharacter());
        }

        return sb.toString();
    }

    private static boolean isExcludedCharacter(char c) {
        return c == '"' || c == ';';
    }

    private static char getRandomUppercase() {
        return CHARACTERS.charAt(new SecureRandom().nextInt(26));
    }

    private static char getRandomNumber() {
        return CHARACTERS.charAt(new SecureRandom().nextInt(10) + 52);
    }

    private static char getRandomSpecialCharacter() {
        return CHARACTERS.charAt(new SecureRandom().nextInt(16) + 62);
    }
}