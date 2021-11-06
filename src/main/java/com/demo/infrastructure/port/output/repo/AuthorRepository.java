package com.demo.infrastructure.port.output.repo;

import com.demo.infrastructure.port.output.data.Author;
import com.demo.infrastructure.port.output.repo.custom.AuthorCustomRepo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthorRepository extends JpaRepository<Author, UUID>, AuthorCustomRepo {
}
