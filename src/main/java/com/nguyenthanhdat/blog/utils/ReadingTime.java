package com.nguyenthanhdat.blog.utils;

public class ReadingTime {
    private static final int WORDS_PER_MINUTE = 200;

    public static int calculateReadingTime(String content) {
        if (content == null || content.trim().isEmpty()) {
            return 1;
        }

        int wordCount = content.trim().split("\\s+").length;

        return Math.max(1, (int) Math.ceil((double) wordCount / WORDS_PER_MINUTE));
    }
}
