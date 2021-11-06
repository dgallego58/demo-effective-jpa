package com.demo.infrastructure.helper;

import com.demo.infrastructure.port.input.dto.AuthorDTO;
import com.demo.infrastructure.port.output.data.Author;
import com.demo.infrastructure.port.output.data.Book;
import com.demo.infrastructure.port.output.data.Convention;

import java.util.Set;
import java.util.stream.Collectors;

public class AuthorDTOMapper {


    public Author asData(AuthorDTO dto) {
        Set<Book> books = dto.getBooks()
                .stream()
                .map(title -> new Book().setTitle(title))
                .collect(Collectors.toSet());
        Set<Convention> conventions = dto.getLocations()
                .stream()
                .map(location -> new Convention().setLocation(location))
                .collect(Collectors.toSet());
        Author author = new Author().setName(dto.getName());
        books.forEach(author::addBook);
        conventions.forEach(author::addConvention);
        return author;
    }

    public AuthorDTO asDto(Author author) {
        Set<String> books = author.getBooks()
                .stream()
                .map(Book::getTitle)
                .collect(Collectors.toSet());
        Set<String> locations = author.getConventions()
                .stream()
                .map(Convention::getLocation)
                .collect(Collectors.toSet());
        return new AuthorDTO().setName(author.getName()).setBooks(books).setLocations(locations);
    }

}
