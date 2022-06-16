package com.example.warresourcesapi.service;

import com.example.warresourcesapi.model.War;
import com.example.warresourcesapi.repository.WarRepository;
import com.example.warresourcesapi.utils.FileDownloader;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.warresourcesapi.utils.CSVOpener.*;

@Service
public class WarService {

    private final Logger logger = LoggerFactory.getLogger(WarService.class);
    private final WarRepository repository;

    public WarService(WarRepository repository) {
        this.repository = repository;
    }

    public List<War> getWars() throws IOException, InterruptedException {
        if (repository.count() == 0) {

            //TODO: zamineić na logger SLF4J
            logger.info("Resources repository empty! Downloading resources");
            String csv = FileDownloader.downloadJSON("https://raw.githubusercontent.com/Jackhalabardnik/wars/master/INTRA-STATE_State_participants%20v5.1%20CSV.csv");
            ArrayList<String[]> arrayList = csvToArray(csv);
            if(arrayList == null)
                throw new RuntimeException("Aray with records is empty!");
            ArrayList<War> wars = arrayToWars(arrayList);
            logger.info("Saving resource to repository");
            repository.saveAll(wars);
        }
        return repository.findAll();
    }

}
