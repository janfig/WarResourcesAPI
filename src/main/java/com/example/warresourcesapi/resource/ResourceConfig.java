//package com.example.warresourcesapi.resource;
//
//import com.example.warresourcesapi.utils.FileDownloader;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.ArrayList;
//
//import static com.example.warresourcesapi.utils.CSVOpener.arrayToResources;
//import static com.example.warresourcesapi.utils.CSVOpener.csvToArray;
//
//@Configuration
//public class ResourceConfig {
//
//    @Bean
//    CommandLineRunner commandLineRunner(ResourceRepository repository) {
//        return args -> {
//            ArrayList<String[]> arrayList = csvToArray(FileDownloader.getResPath() + "gold.csv");
//            ArrayList<Resource> resources = arrayToResources(arrayList);
//            repository.saveAll(resources);
//        };
//    }
//}
