package com.demo.infrastructure.port.output.data.views;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.demo.infrastructure.port.output.data.Book;

import java.util.UUID;

@EntityView(value = Book.class)
public interface BookView {

    @IdMapping
    UUID getId();

    String getTitle();

}
