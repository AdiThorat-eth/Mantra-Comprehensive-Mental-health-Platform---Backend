package com.mantra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.mantra")
@EnableJpaRepositories(basePackages = "com.mantra")
@EntityScan(basePackages = "com.mantra")
@EnableJpaAuditing
public class MantraApplication {
    public static void main(String[] args) {
        SpringApplication.run(MantraApplication.class, args);
    }
}
