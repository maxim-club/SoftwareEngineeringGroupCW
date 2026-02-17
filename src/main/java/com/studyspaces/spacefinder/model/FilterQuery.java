package com.studyspaces.spacefinder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Filter Query from user. null means indiffernece
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterQuery {
    public NoiseLevel preferredNoiseLevel;
    public Occupancy preferredOccupancy;
    public Amenities preferredAmenities;
    public Boolean preferredGroupSpace;
    public Integer preferredGroupSize;
}
