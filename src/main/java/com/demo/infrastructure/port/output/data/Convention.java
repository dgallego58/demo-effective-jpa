package com.demo.infrastructure.port.output.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;


@Table(name = "convention", indexes = {
        @Index(name = "idx_convention_location", columnList = "location"),
        @Index(name = "idx_convention_author_id", columnList = "author_id")
})
@Entity
public class Convention {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "location")
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "convention_author_fk"))
    private Author author;

    public UUID getId() {
        return id;
    }

    public Convention setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public Convention setLocation(String location) {
        this.location = location;
        return this;
    }

    public Author getAuthor() {
        return author;
    }

    public Convention setAuthor(Author author) {
        this.author = author;
        return this;
    }
}
