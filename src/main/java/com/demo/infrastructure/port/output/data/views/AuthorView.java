package com.demo.infrastructure.port.output.data.views;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.demo.infrastructure.port.output.data.Author;

import java.util.List;
import java.util.UUID;

;


@EntityView(Author.class)
public interface AuthorView {

    @IdMapping
    UUID getId();

    String getName();

    @Mapping("books.title")
    List<String> getBooks();

    @Mapping("conventions.location")
    List<String> getConventions();

}
