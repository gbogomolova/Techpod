package com.example.techpod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

// Запуск системы
@SpringBootApplication
public class TechpodApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(TechpodApplication.class, args);
    }

}

