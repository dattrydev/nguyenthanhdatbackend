package com.nguyenthanhdat.blog.domain.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tags")
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
    private String name;

    @JsonIgnore
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
