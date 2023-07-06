package com.kibikalo.pastebin.service;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class HashGenerator {
    public String generateHash(String objectName) {
        String input = objectName;

        try {
            // Create a MessageDigest instance with SHA-256 algorithm
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Generate the hash value from the input string
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convert the hash bytes to a hexadecimal representation
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            // Truncate the hash to 8 characters
            String truncatedHash = hexString.toString().substring(0, 8);

            return truncatedHash;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hash generation failed", e);
        }
    }
}
