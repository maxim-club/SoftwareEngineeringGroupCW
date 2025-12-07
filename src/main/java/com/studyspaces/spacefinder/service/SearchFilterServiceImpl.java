package com.studyspaces.spacefinder.service;

import com.studyspaces.spacefinder.model.RoomData;
import com.studyspaces.spacefinder.model.SimulatedData;
import com.studyspaces.spacefinder.model.SearchFilter;
import com.studyspaces.spacefinder.repository.RoomDataRepository;
import com.studyspaces.spacefinder.repository.SimulatedDataRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SearchFilterServiceImpl implements SearchFilterService {

    private final RoomDataRepository roomRepo;
    private final SimulatedDataRepository simRepo;

    public SearchFilterServiceImpl(RoomDataRepository roomRepo, SimulatedDataRepository simRepo) {
        this.roomRepo = roomRepo;
        this.simRepo = simRepo;
    }

    @Override
    public List<SearchFilter> getAllFilters() {
        List<RoomData> rooms = roomRepo.findAll();
        List<SimulatedData> simulated = simRepo.findAll();

        Map<String, String> simMap = simulated.stream()
                .collect(Collectors.toMap(SimulatedData::getRoomId, SimulatedData::getBusynessLevel));

        return rooms.stream().map(room -> {
            String busynessLevel = simMap.getOrDefault(room.getId(), "Unknown");
            return new SearchFilter(
                    room.getId(),
                    room.getLocation(),
                    room.getHasWifi(),
                    room.getHasOutlets(),
                    room.getHasGroupSeating(),
                    room.getIsQuiet(),
                    busynessLevel
            );
        }).collect(Collectors.toList());
    }

    @Override
    public List<SearchFilter> filterSpaces(String location,
                                           Boolean hasWifi,
                                           Boolean hasOutlets,
                                           Boolean hasGroupSeating,
                                           Boolean isQuiet,
                                           String busynessLevel) {

        return getAllFilters().stream()
                .filter(s -> location == null || s.getLocation().equalsIgnoreCase(location))
                .filter(s -> hasWifi == null || s.getHasWifi().equals(hasWifi))
                .filter(s -> hasOutlets == null || s.getHasOutlets().equals(hasOutlets))
                .filter(s -> hasGroupSeating == null || s.getHasGroupSeating().equals(hasGroupSeating))
                .filter(s -> isQuiet == null || s.getIsQuiet().equals(isQuiet))
                .filter(s -> busynessLevel == null || s.getBusynessLevel().equalsIgnoreCase(busynessLevel))
                .collect(Collectors.toList());
    }
}
