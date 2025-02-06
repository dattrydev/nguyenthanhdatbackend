package com.nguyenthanhdat.blog.utils;

public class GenerateSlug {
    public static String generateSlug(String title) {
        return title.toLowerCase().replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("[\\s-]+", " ")
                .replaceAll("[\\s]", "-");
    }
}
