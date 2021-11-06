package com.demo.infrastructure.port.output.repo;

import com.demo.infrastructure.port.output.data.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
}
