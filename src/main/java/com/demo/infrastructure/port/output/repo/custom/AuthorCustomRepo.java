package com.demo.infrastructure.port.output.repo.custom;

import com.demo.infrastructure.port.input.dto.FilterDTO;
import com.demo.infrastructure.port.output.data.Author;

import java.util.List;

public interface AuthorCustomRepo {

    List<Author> authorNPlus1(FilterDTO filter);

    List<Author> authorsMultiFetch(FilterDTO filterDTO);

    List<Author> authorByPartition(FilterDTO filterDTO);

    List<Author> fetchCast(FilterDTO filterDTO);
}
