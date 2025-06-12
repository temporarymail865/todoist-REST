package com.example.todoist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling

public class TodoistApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoistApplication.class, args);
    }

}