package com.example.warresourcesapi.war;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "api/wars")
public class WarController {

    private final WarService service;

    public WarController(WarService service) {
        this.service = service;
    }

    @GetMapping
    public List<War> getResources() throws IOException, InterruptedException {
        return service.getWars();
    }
}
