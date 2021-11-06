package com.demo.infrastructure.port.input.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(type = "enum", allowableValues = {"name", "title", "location"})
public enum Filter {

    @JsonAlias({"name", "NAME"})
    AUTHOR_NAME("name"),
    @JsonAlias({"title", "TITLE"})
    BOOK_TITLE("title"),
    @JsonAlias({"location", "LOCATION"})
    CONVENTION_LOCATION("location");

    private final String keyword;

    Filter(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }
}
