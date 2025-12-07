package com.studyspaces.spacefinder.controller;

import com.studyspaces.spacefinder.model.SearchFilter;
import com.studyspaces.spacefinder.service.SearchFilterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchFilterController {

    private final SearchFilterService service;

    public SearchFilterController(SearchFilterService service) {
        this.service = service;
    }

    @GetMapping("/search-filters")
    public List<SearchFilter> search(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Boolean hasWifi,
            @RequestParam(required = false) Boolean hasOutlets,
            @RequestParam(required = false) Boolean hasGroupSeating,
            @RequestParam(required = false) Boolean isQuiet,
            @RequestParam(required = false) String busynessLevel
    ) {
        return service.filterSpaces(location, hasWifi, hasOutlets, hasGroupSeating, isQuiet, busynessLevel);
    }
}
