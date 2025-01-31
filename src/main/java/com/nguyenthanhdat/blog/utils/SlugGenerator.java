package com.nguyenthanhdat.blog.utils;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class SlugGenerator {
    public static String toSlug(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        // Chuẩn hóa Unicode (loại bỏ dấu tiếng Việt)
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        String slug = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "") // Xóa dấu
                .replaceAll("[^\\w\\s]", "")  // Xóa ký tự đặc biệt
                .replaceAll("\\s+", "-")      // Thay khoảng trắng bằng dấu '-'
                .toLowerCase(Locale.ENGLISH); // Chuyển về chữ thường
        return slug;
    }
}
