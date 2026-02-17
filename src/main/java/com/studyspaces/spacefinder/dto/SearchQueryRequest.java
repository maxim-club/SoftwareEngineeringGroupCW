package com.studyspaces.spacefinder.dto;
import com.studyspaces.spacefinder.model.*;
import lombok.Data;

@Data
public class SearchQueryRequest {
    public String searchBarBuildingQuery;
    public Coordinates userLocation;
    public StudySpaceProfile filters; //Passing a full room that contains what the user desires
}
