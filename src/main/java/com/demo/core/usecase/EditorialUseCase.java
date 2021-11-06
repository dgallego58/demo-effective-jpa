package com.demo.core.usecase;

import com.demo.infrastructure.port.input.dto.AuthorDTO;
import com.demo.infrastructure.port.input.dto.FilterDTO;

import java.util.List;

public interface EditorialUseCase {

    List<AuthorDTO> authorsNPlus1(FilterDTO filterDTO);

    List<AuthorDTO> authorsMultiFetch(FilterDTO filterDTO);

    List<AuthorDTO> authorsQueryParts(FilterDTO filterDTO);

    AuthorDTO register(AuthorDTO authorDTO);

}
