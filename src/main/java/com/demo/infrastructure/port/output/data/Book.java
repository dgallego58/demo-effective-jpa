package com.demo.infrastructure.port.output.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
public class Book {

    @Id
    @GeneratedValue
    private UUID id;

    private String title;

    @ManyToMany(mappedBy = "books")
    private Set<Author> authors;

    public Book() {
        this.id = UUID.randomUUID();
        this.authors = new HashSet<>();
    }

    public UUID getId() {
        return id;
    }

    public Book setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Book setTitle(String title) {
        this.title = title;
        return this;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public Book setAuthors(Set<Author> authors) {
        this.authors = authors;
        return this;
    }
}
