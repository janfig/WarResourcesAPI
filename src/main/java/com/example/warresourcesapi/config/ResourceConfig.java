package com.example.warresourcesapi.config;

import com.example.warresourcesapi.model.Price;
import com.example.warresourcesapi.model.Resource;
import com.example.warresourcesapi.repository.PriceRepository;
import com.example.warresourcesapi.repository.ResourceRepository;
import com.example.warresourcesapi.utils.FileDownloader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.example.warresourcesapi.utils.CSVOpener.arrayToPrices;
import static com.example.warresourcesapi.utils.CSVOpener.csvToArray;
import static com.example.warresourcesapi.utils.FileDownloader.downloadJSON;
import static com.example.warresourcesapi.utils.FileDownloader.fillResource;


@Configuration
public class ResourceConfig {

    @Bean
    CommandLineRunner commandLineRunner(
            ResourceRepository resourceRepository
    ) {
        if(resourceRepository.count() >= 3)
            return null;
        return args -> {
            ArrayList<Resource> resources = new ArrayList<>();
            String json = downloadJSON("https://data.nasdaq.com/api/v3/datasets/LBMA/GOLD.json?api_key=yUmtgiXWovdimytaZ83r&column_index=1");
            Resource gold = new Resource("gold");
            fillResource(json, gold);

            json = downloadJSON("https://data.nasdaq.com/api/v3/datasets/LBMA/SILVER.json?api_key=yUmtgiXWovdimytaZ83r&column_index=1");
            Resource silver = new Resource("silver");
            fillResource(json, silver);

            FileDownloader.download(
                    FileDownloader.redirectLink("https://www.kaggle.com/api/v1/datasets/download/sc231997/crude-oil-price"),
                    "oil.zip"
            );
            FileDownloader.unzip(FileDownloader.getResPath(),"oil.zip");
            ArrayList<String[]> lista = csvToArray(FileDownloader.getResPath(),"crude-oil-price.csv");
            ArrayList<Price> prices = arrayToPrices(lista);
            Resource oil = new Resource("oil");
            oil.setPrices(new HashSet<>(prices));
            System.out.println("Resource " + oil.getName() + " filled.");

            resources.add(gold);
            resources.add(silver);
            resources.add(oil);

            resourceRepository.saveAll(resources);
            System.out.println("Resources saved");
        };
    }
}
