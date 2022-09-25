package ru.practicum.shareit;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShareItApp {
    @SneakyThrows
    public static void main(String[] args) {
        SpringApplication.run(ShareItApp.class, args);
    }
}
