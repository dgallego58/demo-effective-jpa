package com.demo.core.service;

import com.demo.core.usecase.EditorialUseCase;
import com.demo.infrastructure.helper.AuthorDTOMapper;
import com.demo.infrastructure.port.input.dto.AuthorDTO;
import com.demo.infrastructure.port.input.dto.FilterDTO;
import com.demo.infrastructure.port.output.data.views.AuthorView;
import com.demo.infrastructure.port.output.repo.AuthorRepository;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvFactory;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class EditorialService implements EditorialUseCase {

    private static final Logger log = LoggerFactory.getLogger(EditorialService.class);
    private final AuthorRepository authorRepository;
    private final WriterWrapper writerWrapper;
    @PersistenceContext
    private EntityManager entityManager;

    public EditorialService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
        this.writerWrapper = new WriterWrapper();
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

    @Override
    @Transactional(readOnly = true)
    public void streamAuthors(OutputStream outputStream) {
        var mapper = new AuthorDTOMapper();
        final AtomicInteger i = new AtomicInteger();

        try (var authorStreams = authorRepository.streamAll()) {
            authorStreams.forEach(author -> {
                i.incrementAndGet();
                log.info("author Entity: {}", author);
                var dto = mapper.asDto(author);
                writeStream(outputStream, dto);
                entityManager.detach(author);
            });
        }
        log.info("Records {}", i.get());
    }

    private void writeStream(OutputStream outputStream, AuthorDTO authorDto) {
        log.info("writing: {}", authorDto);
        try {
            var sequenceWriter = this.writerWrapper.getCsvWriter(outputStream);
            sequenceWriter.write(authorDto);
        } catch (IOException e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            log.error("Streaming error {}", stacktrace);
            throw new NoStreamableDataException("Cannot be streamed", e);
        }
    }


    static class WriterWrapper {
        private final ObjectWriter csvWriter;

        public WriterWrapper() {
            var factory = new CsvFactory();
            factory.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
            var mapper = new CsvMapper(factory);
            var csvSchema = mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY)
                                  .schemaFor(AuthorDTO.class)
                                  .withHeader();
            this.csvWriter = mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY)
                                   .writer(csvSchema);
        }

        public SequenceWriter getCsvWriter(OutputStream os) throws IOException {
            return csvWriter.writeValues(os);
        }
    }

    public static class NoStreamableDataException extends RuntimeException {
        public NoStreamableDataException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
