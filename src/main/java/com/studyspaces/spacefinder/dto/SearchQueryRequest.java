package com.studyspaces.spacefinder.dto;
import com.studyspaces.spacefinder.model.*;
import lombok.Data;

@Data
public class SearchQueryRequest {
    private String searchBarBuildingQuery;
    private Coordinates userLocation;
    private FilterQuery filters;
}
