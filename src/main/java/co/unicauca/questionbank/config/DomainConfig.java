package co.unicauca.questionbank.config;

import co.unicauca.questionbank.domain.service.QuizAssembler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Registers the domain services as beans, so the domain package itself needs no Spring annotations. */
@Configuration
public class DomainConfig {
    /** @return the quiz assembler domain service */
    @Bean
    public QuizAssembler quizAssembler() { return new QuizAssembler(); }
}
