package com.demo.infrastructure.port.input.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashSet;
import java.util.Set;

@Schema(description = "The author to be created with his books and convention locations",
        example = "{\n" +
                "  \"name\": \"Gabriel García Márquez\",\n" +
                "  \"books\": [\n" +
                "    \"Memorias de mis putas tristes\",\n" +
                "    \"Relato de un Náufrago\",\n" +
                "    \"Cien Años de Soledad\",\n" +
                "    \"El coronel no tiene quien le escriba\"\n" +
                "  ],\n" +
                "  \"locations\": [\n" +
                "    \"Aracataca\",\n" +
                "    \"Universidad Nacional de Colombia\"\n" +
                "  ]\n" +
                "}")
public class AuthorDTO {

    private String name;
    private Set<String> books;
    private Set<String> locations;

    public AuthorDTO() {
        this.books = new HashSet<>();
        this.locations = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public AuthorDTO setName(String name) {
        this.name = name;
        return this;
    }

    public Set<String> getBooks() {
        return books;
    }

    public AuthorDTO setBooks(Set<String> books) {
        this.books = books;
        return this;
    }

    public Set<String> getLocations() {
        return locations;
    }

    public AuthorDTO setLocations(Set<String> locations) {
        this.locations = locations;
        return this;
    }
}
