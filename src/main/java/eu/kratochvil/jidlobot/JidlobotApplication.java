package eu.kratochvil.jidlobot;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JidlobotApplication {
    public static void main(String[] args) {
        SpringApplication.run(JidlobotApplication.class, args);
    }

    @PostConstruct
    public void init() {

    }
}
