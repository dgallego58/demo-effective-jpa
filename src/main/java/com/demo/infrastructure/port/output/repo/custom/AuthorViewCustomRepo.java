package com.demo.infrastructure.port.output.repo.custom;

import com.demo.infrastructure.port.input.dto.FilterDTO;
import com.demo.infrastructure.port.output.data.views.AuthorView;

import java.util.List;

public interface AuthorViewCustomRepo {

    List<AuthorView> findAllByFilterDTO(FilterDTO filterDTO);
}
