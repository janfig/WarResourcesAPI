package com.example.warresourcesapi.controller;

import com.example.warresourcesapi.model.Resource;
import com.example.warresourcesapi.service.ResourceService;
import com.example.warresourcesapi.model.views.ResourceView;
import com.example.warresourcesapi.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/resources")
public class ResourceController {

    private final ResourceService resourceService;
    private final UserService userService;

    public ResourceController(ResourceService service, UserService userService) {
        this.resourceService = service;
        this.userService = userService;
    }

    @GetMapping
    @JsonView(ResourceView.Basic.class)
    public List<Resource> getAll() {
        return resourceService.getResources();
    }

    @GetMapping("name/{name}")
    public Resource getByName(@PathVariable String name) {
        return resourceService.getResourceByName(name);
    }

    @GetMapping("id/{id}")
    public ResponseEntity<Resource> getById(
            @PathVariable("id") Long resourceId,
            @RequestParam("start_date") Optional<String> startDate,
            @RequestParam("end_date") Optional<String> endDate
    ) {
        Long userId = userService.getAuthId();
        if (startDate.isPresent() && endDate.isPresent()) {
            return new ResponseEntity<>(
                    resourceService.getResourcesFromDateRange(
                            resourceId,
                            startDate.get(),
                            endDate.get(),
                            userId
                    ),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resourceService.getSingleResource(resourceId, userId), HttpStatus.OK);
        }
    }


}
