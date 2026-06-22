package ru.otus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Java Developer. Advanced — открытый урок «Контейнеризация Java-приложений с Docker».
 *
 * Минимальное Spring Boot приложение для демонстрации упаковки сервиса в Docker-образ.
 */
@SpringBootApplication
public class HelloApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloApplication.class, args);
    }

}
