package com.blitz.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        log.info("=== Application Starting ===");
        log.debug("Debug log test");
        SpringApplication.run(Application.class, args);
        log.info("=== Application Started ===");
    }
}
