package com.nguyenthanhdat.blog.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tags", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    @NotNull(message = "Tag name cannot be null")
    @Size(min = 2, max = 100, message = "Tag name must be between 2 and 100 characters")
    private String name;

    @Column(nullable = false, unique = true)
    @NotNull(message = "Tag slug cannot be null")
    @Size(min = 3, max = 100, message = "Tag slug must be between 3 and 100 characters")
    private String slug;

    @ManyToMany(mappedBy = "tags")
    private Set<Post> posts;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id) && Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
