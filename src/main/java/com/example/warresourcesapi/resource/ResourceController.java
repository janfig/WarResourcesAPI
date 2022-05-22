package com.example.warresourcesapi.resource;

import com.example.warresourcesapi.utils.CSVOpener;
import com.example.warresourcesapi.utils.FileDownloader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "api/resources")
public class ResourceController {

    private final ResourceService service;

    public ResourceController(ResourceService service) {
        this.service = service;
    }

    @GetMapping
    public List<Resource> getResources() throws IOException, InterruptedException {
        return service.getResources();
    }

    @GetMapping("{id}")
    public Resource getSingleResource(@PathVariable Long id) {
        return service.getSingleResource(id);
    }

}
