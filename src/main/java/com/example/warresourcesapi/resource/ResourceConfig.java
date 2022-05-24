//package com.example.warresourcesapi.resource;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//
//
//@Configuration
//public class ResourceConfig {
//
//    @Bean
//    CommandLineRunner commandLineRunner(ResourceRepository repository) {
//        return args -> {
//            ArrayList<Resource> resources = new ArrayList<>(Arrays.asList(
//                    new Resource("gold"),
//                    new Resource("silver"),
//                    new Resource("crude_oil")
//            ));
//            repository.saveAll(resources);
//        };
//    }
//}
