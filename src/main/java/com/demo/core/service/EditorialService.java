package com.demo.core.service;

import com.demo.core.usecase.EditorialUseCase;
import com.demo.infrastructure.helper.AuthorDTOMapper;
import com.demo.infrastructure.port.input.dto.AuthorDTO;
import com.demo.infrastructure.port.input.dto.FilterDTO;
import com.demo.infrastructure.port.output.data.views.AuthorView;
import com.demo.infrastructure.port.output.repo.AuthorRepository;

import java.util.List;
import java.util.stream.Collectors;

public class EditorialService implements EditorialUseCase {

    private final AuthorRepository authorRepository;

    public EditorialService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public List<AuthorDTO> authorsNPlus1(FilterDTO filterDTO) {
        return authorRepository.authorNPlus1(filterDTO)
                .stream()
                .map(author -> new AuthorDTOMapper().asDto(author))
                .collect(Collectors.toList());
    }

    @Override
    public List<AuthorDTO> authorsMultiFetch(FilterDTO filterDTO) {
        return authorRepository.authorsMultiFetch(filterDTO)
                .stream()
                .map(author -> new AuthorDTOMapper().asDto(author))
                .collect(Collectors.toList());
    }

    @Override
    public List<AuthorDTO> authorsQueryParts(FilterDTO filterDTO) {
        return authorRepository.authorByPartition(filterDTO)
                .stream()
                .map(author -> new AuthorDTOMapper().asDto(author))
                .collect(Collectors.toList());
    }

    @Override
    public AuthorDTO register(AuthorDTO authorDTO) {
        AuthorDTOMapper mapper = new AuthorDTOMapper();
        var result = authorRepository.save(mapper.asData(authorDTO));
        return mapper.asDto(result);
    }

    @Override
    public List<AuthorView> authorFetch(FilterDTO filterDTO) {
        return authorRepository.findAllByFilterDTO(filterDTO);
    }
}
