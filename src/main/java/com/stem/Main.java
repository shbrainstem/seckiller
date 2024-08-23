package com.stem;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan
public class Main {
    public static void main(String[] args) {

        System.out.println("Hello world!");
        SpringApplication.run(Main.class, args);
    }
}
