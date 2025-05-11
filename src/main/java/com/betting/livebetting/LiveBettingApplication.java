package com.betting.livebetting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LiveBettingApplication {

    public static void main(String[] args) {
        SpringApplication.run(LiveBettingApplication.class, args);
    }

}
