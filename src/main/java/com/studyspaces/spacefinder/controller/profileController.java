package com.studyspaces.spacefinder.controller;

import com.studyspaces.spacefinder.dto.spaceProfileDTO;
import com.studyspaces.spacefinder.service.profileService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/spaces")
public class profileController {
    private final profileService profileService;

    public profileController(profileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/{id}/profile")
    public spaceProfileDTO getSpaceProfile(@PathVariable Long id) {
        return profileService.getSpaceProfile(id);
    }
}
