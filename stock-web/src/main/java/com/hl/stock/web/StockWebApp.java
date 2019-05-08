package com.hl.stock.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class StockWebApp {
    public static void main(String[] args) {
        SpringApplication.run(StockWebApp.class, args);

    }
}
