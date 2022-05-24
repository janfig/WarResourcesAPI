package com.example.warresourcesapi.resource;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "api/resources")
public class ResourceController {

    private final ResourceService service;

    public ResourceController(ResourceService service) {
        this.service = service;
    }

    @GetMapping
    @JsonView(ResourceView.Basic.class)
    public List<Resource> getAll() throws IOException, InterruptedException {
        return service.getResources();
    }

    @GetMapping("id/{id}")
    public Resource getSingleResource(@PathVariable Long id) {
        return service.getSingleResource(id);
    }

    @GetMapping("name/{name}")
    public Resource getResourceById(@PathVariable String name) {
        return service.getResourceByName(name);
    }



}
