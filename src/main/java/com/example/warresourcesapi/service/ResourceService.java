package com.example.warresourcesapi.service;

import com.example.warresourcesapi.exception.NotFoundException;
import com.example.warresourcesapi.model.Price;
import com.example.warresourcesapi.model.Resource;
import com.example.warresourcesapi.repository.ResourceRepository;
import com.example.warresourcesapi.utils.FileDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.example.warresourcesapi.utils.CSVOpener.*;


@Service
public class ResourceService {

    private final Logger logger = LoggerFactory.getLogger(ResourceService.class);
    private final ResourceRepository resourceRepository;

    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }


    public void fetchRecords() throws IOException, InterruptedException {
        Resource resource = new Resource("gold");
        Resource resource2 = new Resource("oil");
        logger.info("Resources repository empty! Downloading resources");
        FileDownloader.download(
                FileDownloader.redirectLink("https://www.kaggle.com/api/v1/datasets/download/omdatas/historic-gold-prices"),
                "gold.zip");
        logger.info("Unzipping file");
        FileDownloader.unzip(FileDownloader.getResPath(), "gold.zip");
        ArrayList<String[]> arrayList = csvToArray(FileDownloader.getResPath(), "goldx.csv");
        ArrayList<Price> prices = arrayToPrices(arrayList);
        HashSet<Price> prices2 = new HashSet<>();
        prices2.add(new Price(22.22, LocalDate.of(20, 2, 2)));
        logger.info("Saving resource to repository");
        resource.setPrices(new HashSet<>(prices));
        resource2.setPrices(prices2);
//        System.out.println(resource);
        resourceRepository.save(resource);
        resourceRepository.save(resource2);
    }

    public List<Resource> getResources() {
        return resourceRepository.findAll();

    }

    public Resource getSingleResource(Long id) {
       return resourceRepository.findById(id)
               .orElseThrow(() -> new NotFoundException("There is no resource with given id"));
    }

    public Resource getResourceByName(String name) {
        return resourceRepository.getByName(name);
    }

    public Resource getResourcesFromDateRange(Long id, String startDateStr, String endDateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        var startDate = LocalDate.parse(startDateStr, formatter);
        var endDate = LocalDate.parse(endDateStr, formatter);
//        return resourceRepository.getResourcesByPricesBetween(id, startDate, endDate);
        return new Resource(
                id,
                resourceRepository.getById(id).getName(),
                resourceRepository.getResourceByIdAndPricesBetween(id, startDate, endDate)
        );
    }



}
