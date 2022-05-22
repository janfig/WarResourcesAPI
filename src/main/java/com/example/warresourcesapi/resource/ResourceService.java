package com.example.warresourcesapi.resource;

import com.example.warresourcesapi.utils.FileDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.warresourcesapi.utils.CSVOpener.arrayToResources;
import static com.example.warresourcesapi.utils.CSVOpener.csvToArray;


@Service
public class ResourceService {

    private final Logger logger = LoggerFactory.getLogger(ResourceService.class);
    private final ResourceRepository repository;

    public ResourceService(ResourceRepository repository) {
        this.repository = repository;
    }

    public void fetchRecords() throws IOException, InterruptedException {
        logger.info("Resources repository empty! Downloading resources");
        FileDownloader.download(
                FileDownloader.redirectLink("https://www.kaggle.com/api/v1/datasets/download/omdatas/historic-gold-prices"),
                "gold.zip");
        logger.info("Unzipping file");
        FileDownloader.unzip(FileDownloader.getResPath(), "gold.zip");
        ArrayList<String[]> arrayList = csvToArray(FileDownloader.getResPath(), "goldx.csv");
        ArrayList<Resource> resources = arrayToResources(arrayList);
        logger.info("Saving resource to repository");
        repository.saveAll(resources);
    }

    public List<Resource> getResources() throws IOException, InterruptedException {
        if (repository.count() == 0) {
            fetchRecords();
        }
        return repository.findAll();
    }

    public Resource getSingleResource(Long id) {
        return repository.getById(id);
    }

}
