package com.nguyenthanhdat.blog.utils;

public class SlugGenerator {
    public static String generateSlug(String title) {
        // Convert to lowercase
        String slug = title.toLowerCase();

        // Remove accents and convert to the appropriate characters
        slug = slug.replaceAll("[áàảạãăắằẳẵặâấầẩẫậ]", "a");
        slug = slug.replaceAll("[éèẻẽẹêếềểễệ]", "e");
        slug = slug.replaceAll("[iíìỉĩị]", "i");
        slug = slug.replaceAll("[óòỏõọôốồổỗộơớờởỡợ]", "o");
        slug = slug.replaceAll("[úùủũụưứừửữự]", "u");
        slug = slug.replaceAll("[ýỳỷỹỵ]", "y");
        slug = slug.replaceAll("[đ]", "d");

        // Remove special characters
        slug = slug.replaceAll("[`~!@#|$%^&*()+=,./?><':;_]", "");

        // Replace spaces with hyphens
        slug = slug.replaceAll(" ", "-");

        // Remove multiple hyphens
        slug = slug.replaceAll("-{2,}", "-");

        // Remove hyphens at the start and end
        slug = slug.replaceAll("^-|-$", "");

        return slug;
    }
}
