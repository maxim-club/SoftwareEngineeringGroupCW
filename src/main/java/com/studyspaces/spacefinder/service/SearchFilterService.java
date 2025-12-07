package com.studyspaces.spacefinder.service;

import com.studyspaces.spacefinder.model.SearchFilter;
import java.util.List;

public interface SearchFilterService {
    List<SearchFilter> getAllFilters();

    List<SearchFilter> filterSpaces(String location,
                                    Boolean hasWifi,
                                    Boolean hasOutlets,
                                    Boolean hasGroupSeating,
                                    Boolean isQuiet,
                                    String busynessLevel);
}
