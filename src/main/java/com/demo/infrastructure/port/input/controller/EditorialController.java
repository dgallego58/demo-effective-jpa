package com.demo.infrastructure.port.input.controller;

import com.demo.core.usecase.EditorialUseCase;
import com.demo.infrastructure.helper.JacksonUtil;
import com.demo.infrastructure.port.input.controller.swagger.EditorialSwagger;
import com.demo.infrastructure.port.input.dto.AuthorDTO;
import com.demo.infrastructure.port.input.dto.FilterDTO;
import io.micrometer.core.annotation.Timed;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/editorial")
public class EditorialController implements EditorialSwagger {

    private final EditorialUseCase editorialUseCase;

    public EditorialController(EditorialUseCase editorialUseCase) {
        this.editorialUseCase = editorialUseCase;
    }

    @Override
    @Timed(description = "Metric for author insertion",
            extraTags = {"cardinality", "singular",
                    "formato", "json",
                    "info", "author",
                    "presentation", "report"},
            value = "insert_author")
    public ResponseEntity<AuthorDTO> register(AuthorDTO authorDTO) {
        return ResponseEntity.ok(editorialUseCase.register(authorDTO));
    }

    @Override
    @Timed(description = "Metric for fetch author with N + 1",
            extraTags = {"cardinality", "multiple",
                    "formato", "json",
                    "info", "author",
                    "presentation", "report"},
            value = "nplus_fetch")
    public ResponseEntity<List<AuthorDTO>> nPlus1Fetch(FilterDTO filterDTO) {
        return ResponseEntity.ok(editorialUseCase.authorsNPlus1(filterDTO));
    }

    @Override
    @Timed(description = "Metric for in memory pagination",
            extraTags = {"cardinality", "multiple",
                    "formato", "json",
                    "info", "author",
                    "presentation", "report"},
            value = "memory_page")
    public ResponseEntity<List<AuthorDTO>> inMemoryPaginationFetch(FilterDTO filterDTO) {
        return ResponseEntity.ok(editorialUseCase.authorsMultiFetch(filterDTO));
    }

    @Override
    @Timed(description = "Metric for in query parts",
            extraTags = {"cardinality", "multiple",
                    "formato", "json",
                    "info", "author",
                    "presentation", "report"},
            value = "partition_fetch")
    public ResponseEntity<List<AuthorDTO>> partitionFetch(FilterDTO filterDTO) {
        return ResponseEntity.ok(editorialUseCase.authorsQueryParts(filterDTO));
    }
}
