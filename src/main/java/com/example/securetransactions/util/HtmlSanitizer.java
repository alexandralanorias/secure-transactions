package com.example.securetransactions.util;

public final class HtmlSanitizer {
    private HtmlSanitizer() {}

    // Very conservative: strip angle brackets to prevent basic HTML/script injection in descriptions.
    public static String sanitize(String input) {
        if (input == null) return null;
        // Remove common dangerous tags keywords and angle brackets
        String stripped = input.replaceAll("(?i)</?script[^>]*>", "")
                .replace("<", "")
                .replace(">", "");
        // Collapse control characters (except newline and tab)
        stripped = stripped.replaceAll("[\\p{Cntrl}&&[^\\n\\t]]", "");
        return stripped;
    }
}