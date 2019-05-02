package com.hl.stock;

import com.hl.stock.fx.FxApp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class StockApp {
    public static void main(String[] args) {
        SpringApplication.run(StockApp.class, args);
        FxApp.startApp(args);
    }
}
