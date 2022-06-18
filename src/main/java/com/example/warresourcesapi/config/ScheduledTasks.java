package com.example.warresourcesapi.config;

import com.example.warresourcesapi.model.Price;
import com.example.warresourcesapi.repository.ResourceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;

import static com.example.warresourcesapi.utils.CSVOpener.csvToArray;
import static com.example.warresourcesapi.utils.FileDownloader.*;

@Component
@Slf4j
public class ScheduledTasks {

    private final ResourceRepository resourceRepository;

    public ScheduledTasks(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        LocalDate currentDate = LocalDate.now();
        HashMap<String, Price> prices = new HashMap<>();

        String json = downloadJSON("https://data.nasdaq.com/api/v3/datasets/LBMA/GOLD.json?api_key=yUmtgiXWovdimytaZ83r&column_index=1"
                + "&start_date=" + currentDate.toString());
        prices.put("gold", getPriceFromNasdaq(json));
        System.out.println(prices);
    }

    @Scheduled(cron = "0 0 10 * * *")
    public void reportCurrentTime() throws IOException, InterruptedException {
        LocalDate currentDate = LocalDate.now();

        String json = downloadJSON("https://data.nasdaq.com/api/v3/datasets/LBMA/GOLD.json?api_key=yUmtgiXWovdimytaZ83r&column_index=1"
        + "&start_date=" + currentDate.toString() + "&end_date=" + currentDate.toString());
        Price goldPrice = getPriceFromNasdaq(json);
        var gold = resourceRepository.getByName("gold");
        gold.getPrices().add(goldPrice);
        resourceRepository.save(gold);

        json = downloadJSON("https://data.nasdaq.com/api/v3/datasets/LBMA/SILVER.json?api_key=yUmtgiXWovdimytaZ83r&column_index=1"
                + "&start_date=" + currentDate.toString() + "&end_date=" + currentDate.toString());
        Price silverPrice = getPriceFromNasdaq(json);
        var silver = resourceRepository.getByName("gold");
        gold.getPrices().add(silverPrice);
        resourceRepository.save(silver);


        json = downloadJSON("https://api.eia.gov/v2/natural-gas/pri/fut/data?api_key=D4umPxd4ER1AkOrwTo38jsztp54OgzRba7YiFKay&frequency=daily&facets%5Bseries%5D%5B%5D=RNGWHHD&sort%5B0%5D%5Bcolumn%5D=period&sort%5B0%5D%5Bdirection%5D=desc&data%5B1%5D=value&length=12830"
                + "&start" + currentDate.toString());
        Price gasPrice = getPriceFromEia(json);
        var gas = resourceRepository.getByName("natural gas");
        gold.getPrices().add(gasPrice);
        resourceRepository.save(gas);
    }
}