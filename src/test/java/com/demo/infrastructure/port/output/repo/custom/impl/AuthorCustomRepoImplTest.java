package com.demo.infrastructure.port.output.repo.custom.impl;

import com.demo.infrastructure.port.input.dto.Filter;
import com.demo.infrastructure.port.input.dto.FilterDTO;
import com.demo.infrastructure.port.output.data.Author;
import com.demo.infrastructure.port.output.data.Book;
import com.demo.infrastructure.port.output.data.Convention;
import com.demo.infrastructure.port.output.repo.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@SuppressWarnings(value = {"rawtypes"})
class AuthorCustomRepoImplTest {

    private static final Logger log = LoggerFactory.getLogger(AuthorCustomRepoImplTest.class);
    @Container
    static PostgreSQLContainer database = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("testing")
            .withUsername("testing")
            .withPassword("testing");
    @Autowired
    AuthorRepository authorRepository;

    @DynamicPropertySource
    static void configDataSourceProperty(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", database::getJdbcUrl);
        registry.add("spring.datasource.username", database::getUsername);
        registry.add("spring.datasource.password", database::getPassword);
    }

    @BeforeEach
    void setUp() {
        authorRepository.saveAllAndFlush(Arrays.asList(gabo(), caycedo()));
    }

    @Test
    void testNPlus1() {
        log.info("Iniciando problema N + 1");
        var result = authorRepository.authorNPlus1(filterDTO());
        assertThat(result).isNotEmpty()
                .first()
                .matches(author -> author.getBooks()
                        .stream()
                        .anyMatch(book -> book.getTitle().contains("mis")))
                .matches(author -> author.getConventions()
                        .stream()
                        .anyMatch(convention -> "Aracataca".equals(convention.getLocation())));
        var bookTitle = result.stream().flatMap(author -> author.getBooks().stream()).map(Book::getTitle)
                .collect(Collectors.toList());
        var conventionLocation = result.stream().flatMap(author -> author.getConventions().stream())
                .map(Convention::getLocation).collect(Collectors.toList());

        log.info("Conventions are {} Books are {}", conventionLocation, bookTitle);
    }

    @Test
    void testMultiFetch() {
        log.info("Iniciando problema Multiples fetch con to-Many relationships");
        var result = authorRepository.authorsMultiFetch(filterDTO());
        assertThat(result).isNotEmpty()
                .first()
                .matches(author -> author.getBooks()
                        .stream()
                        .anyMatch(book -> book.getTitle().contains("mis")))
                .matches(author -> author.getConventions()
                        .stream()
                        .anyMatch(convention -> "Aracataca".equals(convention.getLocation())));
        var bookTitle = result.stream().flatMap(author -> author.getBooks().stream()).map(Book::getTitle)
                .collect(Collectors.toList());
        var conventionLocation = result.stream().flatMap(author -> author.getConventions().stream())
                .map(Convention::getLocation).collect(Collectors.toList());
        log.info("Conventions are {} Books are {}", conventionLocation, bookTitle);
    }

    @Test
    void testParts() {
        log.info("Iniciando solución Multiples queries haciendo consultas por cada relación usando fetch con to-Many relationships");
        var result = authorRepository.authorByPartition(filterDTO());
        assertThat(result).isNotEmpty()
                .first()
                .matches(author -> author.getBooks()
                        .stream()
                        .anyMatch(book -> book.getTitle().contains("mis")))
                .matches(author -> author.getConventions()
                        .stream()
                        .anyMatch(convention -> "Aracataca".equals(convention.getLocation())));
        var bookTitle = result.stream().flatMap(author -> author.getBooks().stream()).map(Book::getTitle)
                .collect(Collectors.toList());
        var conventionLocation = result.stream().flatMap(author -> author.getConventions().stream())
                .map(Convention::getLocation).collect(Collectors.toList());
        log.info("Conventions are {} Books are {}", conventionLocation, bookTitle);
    }


    private FilterDTO filterDTO() {
        EnumMap<Filter, Object> filterObjectEnumMap = new EnumMap<>(Filter.class);
        filterObjectEnumMap.put(Filter.AUTHOR_NAME, "e");
        filterObjectEnumMap.put(Filter.BOOK_TITLE, "ad");
        filterObjectEnumMap.put(Filter.CONVENTION_LOCATION, "Nacional");
        return new FilterDTO().setFilters(filterObjectEnumMap);
    }

    private Author gabo() {
        Author author = new Author().setName("Gabriel García Márquez");
        author.addBook(new Book().setTitle("Cien Años de Soledad"));
        author.addBook(new Book().setTitle("Memorias de mis putas tristes"));
        author.addConvention(new Convention().setLocation("Aracataca"));
        author.addConvention(new Convention().setLocation("Universidad Nacional de Colombia"));
        return author;
    }

    private Author caycedo() {
        Author author = new Author().setName("Germán Castro Caycedo");
        author.addBook(new Book().setTitle("Perdido en el Amazonas"));
        author.addBook(new Book().setTitle("Hágase tu voluntad"));
        author.addConvention(new Convention().setLocation("Zipaquirá"));
        author.addConvention(new Convention().setLocation("Universidad Nacional de Colombia"));
        return author;
    }

}
