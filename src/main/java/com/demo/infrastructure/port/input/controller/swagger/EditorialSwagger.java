package com.demo.infrastructure.port.input.controller.swagger;

import com.demo.infrastructure.port.input.dto.AuthorDTO;
import com.demo.infrastructure.port.input.dto.FilterDTO;
import com.demo.infrastructure.port.output.data.views.AuthorView;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;

@RequestMapping(path = "/default-editorial-path")
public interface EditorialSwagger {

    @PostMapping(path = "/register-author")
    ResponseEntity<AuthorDTO> register(@RequestBody AuthorDTO authorDTO);

    @Operation(description = "Gets all the authors by the given filters")
    @PostMapping(path = "/authors-with-n-plus1")
    ResponseEntity<List<AuthorDTO>> nPlus1Fetch(
            @RequestBody @Schema(description = "the request object to filter") FilterDTO filterDTO);

    @Operation(description = "Gets all the authors by the given filters")
    @PostMapping(path = "/authors-with-memory-pagination")
    ResponseEntity<List<AuthorDTO>> inMemoryPaginationFetch(
            @RequestBody @Schema(description = "the request object to filter") FilterDTO filterDTO);

    @Operation(description = "Gets all the authors by the given filters")
    @PostMapping(path = "/authors-with-partition")
    ResponseEntity<List<AuthorDTO>> partitionFetch(
            @RequestBody @Schema(description = "the request object to filter") FilterDTO filterDTO);

    @Operation(description = "Gets all the authors by the given filters")
    @PostMapping(path = "/authors-view")
    ResponseEntity<List<AuthorView>> authorView(
            @RequestBody @Schema(description = "the request object to filter") FilterDTO filterDTO);


    @Timed(description = "Metric for in stream parts",
            extraTags = {"cardinality", "multiple",
                    "formato", "json",
                    "info", "author",
                    "presentation", "report"},
            value = "partition_fetch")
    @GetMapping(path = "/authors-stream")
    ResponseEntity<StreamingResponseBody> streamAuthors();
}
