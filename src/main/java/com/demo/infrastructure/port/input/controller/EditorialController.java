package com.demo.infrastructure.port.input.controller;

import com.demo.core.usecase.EditorialUseCase;
import com.demo.infrastructure.port.input.controller.swagger.EditorialSwagger;
import com.demo.infrastructure.port.input.dto.AuthorDTO;
import com.demo.infrastructure.port.input.dto.FilterDTO;
import com.demo.infrastructure.port.output.data.views.AuthorView;
import io.micrometer.core.annotation.Timed;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;
import java.util.stream.IntStream;

@RestController
@RequestMapping(path = "/editorial")
public class EditorialController implements EditorialSwagger {

    private final EditorialUseCase editorialUseCase;

    public EditorialController(EditorialUseCase editorialUseCase) {
        this.editorialUseCase = editorialUseCase;
    }

    /**
     * add IntStream.range(0, 9_999).mapToObj(value -> authorDTO).forEach(editorialUseCase::register);
     */
    @Override
    @Timed(description = "Metric for author insertion",
            extraTags = {"cardinality", "singular",
                    "formato", "json",
                    "info", "author",
                    "presentation", "report"},
            value = "insert_author")
    public ResponseEntity<AuthorDTO> register(final AuthorDTO authorDTO) {
        IntStream.range(0, 999_999)
                 .mapToObj(value -> authorDTO.setName(String.valueOf(value)))
                 .forEach(editorialUseCase::register);
        return ResponseEntity.ok().header("custom-head", "test").body(editorialUseCase.register(authorDTO));
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

    @Override
    @Timed(description = "Metric for in query parts",
            extraTags = {"cardinality", "multiple",
                    "formato", "json",
                    "info", "author",
                    "presentation", "report"},
            value = "partition_fetch")
    public ResponseEntity<List<AuthorView>> authorView(FilterDTO filterDTO) {
        return ResponseEntity.ok(editorialUseCase.authorFetch(filterDTO));
    }

    @Override
    @Timed(description = "Metric for in stream parts",
            extraTags = {"cardinality", "multiple",
                    "formato", "json",
                    "info", "author",
                    "presentation", "report"},
            value = "partition_fetch")
    public ResponseEntity<StreamingResponseBody> streamAuthors() {

        StreamingResponseBody responseBody = editorialUseCase::streamToFile;
        return ResponseEntity.ok()
                             .header("Content-Disposition", "attachment;filename=download.csv")
                             .header("Content-Type", "application/csv;charset=UTF-8")
                             .body(responseBody);
    }

    @Override
    @Timed(description = "Metric for in stream parts",
            extraTags = {"cardinality", "multiple",
                    "formato", "json",
                    "info", "author",
                    "presentation", "report"},
            value = "partition_fetch")
    public ResponseEntity<StreamingResponseBody> streamAuthorsJson() {
        StreamingResponseBody responseBody = editorialUseCase::streamToJson;
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_NDJSON)
                             .body(responseBody);
    }

}
