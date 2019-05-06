package com.hl.stock.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class StockApp {
    public static void main(String[] args) {
        SpringApplication.run(StockApp.class, args);
    }
}
