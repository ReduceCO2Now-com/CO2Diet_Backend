package com.reduceco2now;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Single entry point for the modular monolith. Component scanning starts
 * at "com.reduceco2now", so beans from every module (catalog, shared's
 * exception handler once we add it, future co2/sync/identity...) are
 * discovered automatically as long as they live under that root package.
 */
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}