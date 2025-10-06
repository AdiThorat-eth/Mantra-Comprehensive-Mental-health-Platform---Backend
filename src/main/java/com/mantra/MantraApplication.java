package com.mantra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MantraApplication {
    public static void main(String[] args) {
        SpringApplication.run(MantraApplication.class, args);
    }
}