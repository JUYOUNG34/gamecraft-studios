package com.gamecraft.studios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GameCraftStudiosApplication {
    public static void main(String[] args) {
        SpringApplication.run(GameCraftStudiosApplication.class, args);
    }
}