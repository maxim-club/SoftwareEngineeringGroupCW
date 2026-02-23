package com.studyspaces.spacefinder.dto;

import com.studyspaces.spacefinder.model.StudySpaceProfile;
import lombok.Data;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
public class SearchResponseDTO {
    private List<StudySpaceProfile> exactMatches;
    private List<StudySpaceProfile> recommendations;
}