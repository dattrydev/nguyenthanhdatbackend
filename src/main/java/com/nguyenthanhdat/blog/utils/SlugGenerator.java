package com.nguyenthanhdat.blog.utils;

import java.text.Normalizer;

public class SlugGenerator {
    public static String generateSlug(String title) {
        String normalizedTitle = Normalizer.normalize(title, Normalizer.Form.NFD);
        normalizedTitle = normalizedTitle.replaceAll("[^\\p{ASCII}]", "");

        return normalizedTitle.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("[\\s-]+", " ")
                .replaceAll("[\\s]", "-");
    }
}
