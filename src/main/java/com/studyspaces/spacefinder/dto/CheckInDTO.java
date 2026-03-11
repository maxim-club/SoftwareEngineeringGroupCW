package com.studyspaces.spacefinder.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckInDTO {
    private Boolean closed;
    private Boolean wifiIssue;
    private Boolean reserved;
    private Boolean fullyOccupied;
    private String occupancy;
}
