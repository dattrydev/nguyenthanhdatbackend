package com.nguyenthanhdat.blog.domain.entities;

import com.nguyenthanhdat.blog.domain.enums.PostStatus;
import com.nguyenthanhdat.blog.utils.SlugGenerator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "posts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @NotNull(message = "Post title cannot be null")
    @Size(min = 3, max = 255, message = "Post title must be between 3 and 255 characters")
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotNull(message = "Post content cannot be null")
    private String content;

    @Column(nullable = false)
    @NotNull(message = "Post status cannot be null")
    @Enumerated(EnumType.STRING)
    private PostStatus status;

    @Column(nullable = false)
    @NotNull(message = "Reading time cannot be null")
    private Integer readingTime;

    @Column(nullable = false, unique = true)
    @NotNull(message = "Post slug cannot be null")
    @Size(min = 3, max = 100, message = "Post slug must be between 3 and 100 characters")
    private String slug;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToMany
    @JoinTable(
            name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id) && Objects.equals(title, post.title) && Objects.equals(content, post.content) && status == post.status && Objects.equals(readingTime, post.readingTime) && Objects.equals(createdAt, post.createdAt) && Objects.equals(updatedAt, post.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, status, readingTime, createdAt, updatedAt);
    }

    @PrePersist
    @PreUpdate
    protected void generateSlug() {
        if (slug == null || slug.isEmpty()) {
            slug = SlugGenerator.toSlug(title);
        }
    }
}
