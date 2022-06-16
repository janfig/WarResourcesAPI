package com.example.warresourcesapi.service;

import com.example.warresourcesapi.exception.NotFoundException;
import com.example.warresourcesapi.model.Price;
import com.example.warresourcesapi.model.Resource;
import com.example.warresourcesapi.repository.ResourceRepository;
import com.example.warresourcesapi.utils.FileDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
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
