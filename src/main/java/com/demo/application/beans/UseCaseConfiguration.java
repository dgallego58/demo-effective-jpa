package com.demo.application.beans;

import com.demo.core.service.EditorialService;
import com.demo.core.usecase.EditorialUseCase;
import com.demo.infrastructure.port.output.repo.AuthorRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfiguration {

    @Bean
    public EditorialUseCase editorialUseCase(AuthorRepository authorRepository){
        return new EditorialService(authorRepository);
    }
}
