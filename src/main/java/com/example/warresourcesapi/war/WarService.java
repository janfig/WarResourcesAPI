package com.example.warresourcesapi.war;

import com.example.warresourcesapi.utils.FileDownloader;
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
            logger.info("Resources repository empty! Downloading resources");
            FileDownloader.download(
                    "https://correlatesofwar.org/data-sets/COW-war/intra-state-wars-v5-1.zip/@@download/file/Intra-State%20Wars%20v5.1.zip",
                    "wars.zip"
            );
            logger.info("Unzipping file");
            FileDownloader.unzip(FileDownloader.getResPath(),"wars.zip");
            ArrayList<String[]> arrayList = csvToArray(FileDownloader.getResPath(),"INTRA-STATE WARS v5.1 CSV.csv");
            ArrayList<War> wars = arrayToWars(arrayList);
            logger.info("Saving resource to repository");
            repository.saveAll(wars);
        }
        return repository.findAll();
    }

}
