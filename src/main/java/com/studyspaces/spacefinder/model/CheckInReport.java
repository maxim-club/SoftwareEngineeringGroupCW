package com.studyspaces.spacefinder.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


/* CheckInReport
Model represents the data a user submits when they check into a room
 */
@Data
@AllArgsConstructor
public class CheckInReport {
    private Occupancy occupancy;
    public CheckInReport() {}
}
