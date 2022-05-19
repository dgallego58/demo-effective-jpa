package com.demo.infrastructure.port.output.repo;

import com.demo.infrastructure.port.output.data.Author;
import com.demo.infrastructure.port.output.repo.custom.AuthorCustomRepo;
import com.demo.infrastructure.port.output.repo.custom.AuthorViewCustomRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.UUID;
import java.util.stream.Stream;

import static org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE;

public interface AuthorRepository extends JpaRepository<Author, UUID>, AuthorCustomRepo, AuthorViewCustomRepo {


    @Query("select a from Author a")
    @QueryHints(value = @QueryHint(name = HINT_FETCH_SIZE, value = "0"))
    Stream<Author> streamAll();

}
