package com.example.warresourcesapi.config;

import com.example.warresourcesapi.model.Price;
import com.example.warresourcesapi.model.Resource;
import com.example.warresourcesapi.repository.ResourceRepository;
import com.example.warresourcesapi.repository.RoleRepository;
import com.example.warresourcesapi.utils.CSVOpener;
import com.example.warresourcesapi.utils.FileDownloader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.example.warresourcesapi.utils.CSVOpener.arrayToPrices;
import static com.example.warresourcesapi.utils.CSVOpener.csvToArray;
import static com.example.warresourcesapi.utils.FileDownloader.*;


@Configuration
public class ResourceConfig {

    @Bean
    CommandLineRunner commandLineRunner(
            ResourceRepository resourceRepository,
            RoleRepository roleRepository
    ) {
        if (resourceRepository.count() >= 4)
            return null;
        return args -> {
            ArrayList<Resource> resources = new ArrayList<>();
            String json = downloadJSON("https://data.nasdaq.com/api/v3/datasets/LBMA/GOLD.json?api_key=yUmtgiXWovdimytaZ83r&column_index=1");
            Resource gold = new Resource("gold");
            fillResource(json, gold);

            json = downloadJSON("https://data.nasdaq.com/api/v3/datasets/LBMA/SILVER.json?api_key=yUmtgiXWovdimytaZ83r&column_index=1");
            Resource silver = new Resource("silver");
            fillResource(json, silver);

            json = downloadJSON("https://api.eia.gov/v2/natural-gas/pri/fut/data?api_key=D4umPxd4ER1AkOrwTo38jsztp54OgzRba7YiFKay&frequency=daily&facets%5Bseries%5D%5B%5D=RNGWHHD&sort%5B0%5D%5Bcolumn%5D=period&sort%5B0%5D%5Bdirection%5D=desc&data%5B1%5D=value&length=12830");
            Resource gas = new Resource("natural gas");
            FileDownloader.JsonConverter(json, gas);
            System.out.println("Resource " + gas.getName() + " filled.");

            String csv = FileDownloader.downloadJSON("https://raw.githubusercontent.com/Jackhalabardnik/wars/master/Europe_Brent_Spot_Price_FOB.csv");
            ArrayList<String[]> arrayList = csvToArray(csv);
            if (arrayList == null)
                throw new RuntimeException("Aray with records is empty!");
            Resource oil = new Resource("oil");
            CSVOpener.arrayToOil(arrayList, oil);
            System.out.println("Resource " + oil.getName() + " filled.");

            fillMissingDays(gold);
            fillMissingDays(silver);
            fillMissingDays(oil);
            fillMissingDays(gas);

            resources.add(gold);
            resources.add(silver);
            resources.add(oil);
            resources.add(gas);

            resourceRepository.saveAll(resources);
            System.out.println("Resources saved");
        };
    }
}


