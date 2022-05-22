package com.example.warresourcesapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
public class WarResourcesApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WarResourcesApiApplication.class, args);
    }

}
