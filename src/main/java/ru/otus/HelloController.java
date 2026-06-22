package ru.otus;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * Простой REST-контроллер.
 *
 * Отдаёт приветствие и имя хоста — внутри контейнера это будет ID контейнера,
 * что наглядно показывает изоляцию окружения (см. слайды «Окружение в окружении»).
 */
@RestController
public class HelloController {

    @GetMapping("/")
    public Map<String, String> hello(@RequestParam(defaultValue = "OTUS") String name) {
        return Map.of(
                "message", "Привет, " + name + "! Сервис работает внутри Docker-контейнера.",
                "host", hostName()
        );
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }

    private String hostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "unknown";
        }
    }

}
