package com.example.warresourcesapi.controller;

import com.example.warresourcesapi.model.Resource;
import com.example.warresourcesapi.service.ResourceService;
import com.example.warresourcesapi.views.ResourceView;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/resources")
public class ResourceController {

    private final ResourceService service;

    public ResourceController(ResourceService service) {
        this.service = service;
    }

    @GetMapping
    @JsonView(ResourceView.Basic.class)
    public List<Resource> getAll() {
        return service.getResources();
    }

    @GetMapping("name/{name}")
    public Resource getByName(@PathVariable String name) {
        return service.getResourceByName(name);
    }

    @GetMapping("id/{id}")
    public ResponseEntity<Resource> getById(
            @PathVariable("id") Long id,
            @RequestParam("start_date") Optional<String> startDate,
            @RequestParam("end_date") Optional<String> endDate
    ) {

        if (startDate.isPresent() && endDate.isPresent()) {
            return new ResponseEntity<>(
                    service.getResourcesFromDateRange(
                            id,
                            startDate.get(),
                            endDate.get()
                    ),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(service.getSingleResource(id), HttpStatus.OK);
        }
    }


}
