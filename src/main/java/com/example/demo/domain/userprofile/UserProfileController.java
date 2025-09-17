package com.example.demo.domain.userprofile;

import com.example.demo.domain.user.User;
import com.example.demo.domain.userprofile.dto.UserProfileDTO;
import com.example.demo.domain.userprofile.dto.UserProfileRegisterDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/profiles")
@Tag(name = "User Profile", description = "User Profile management")
@SecurityRequirement(name = "bearerAuth")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    // UC1: Create own profile
    @PostMapping
    @Operation(summary = "Create user profile", description = "User creates their own profile")
    public ResponseEntity<UserProfileDTO> createProfile(
            @Valid @RequestBody UserProfileRegisterDTO registerDTO,
            @AuthenticationPrincipal User currentUser) {
        UserProfileDTO createdProfile = userProfileService.createProfile(registerDTO, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProfile);
    }

    // UC2: Get own profile
    @GetMapping("/me")
    @Operation(summary = "Get own profile", description = "User gets their own profile")
    public ResponseEntity<UserProfileDTO> getOwnProfile(@AuthenticationPrincipal User currentUser) {
        UserProfileDTO profile = userProfileService.getOwnProfile(currentUser);
        return ResponseEntity.ok(profile);
    }

    // UC2: Update own profile
    @PutMapping("/me")
    @Operation(summary = "Update own profile", description = "User updates their own profile")
    public ResponseEntity<UserProfileDTO> updateOwnProfile(
            @Valid @RequestBody UserProfileDTO profileDTO,
            @AuthenticationPrincipal User currentUser) {
        UserProfileDTO updatedProfile = userProfileService.updateOwnProfile(profileDTO, currentUser);
        return ResponseEntity.ok(updatedProfile);
    }

    // UC2: Delete own profile
    @DeleteMapping("/me")
    @Operation(summary = "Delete own profile", description = "User deletes their own profile")
    public ResponseEntity<Void> deleteOwnProfile(@AuthenticationPrincipal User currentUser) {
        userProfileService.deleteOwnProfile(currentUser);
        return ResponseEntity.noContent().build();
    }

    // UC3: Admin gets any profile by ID
    @GetMapping("/{profileId}")
    @Operation(summary = "Get profile by ID", description = "Admin or owner gets profile by ID")
    public ResponseEntity<UserProfileDTO> getProfileById(
            @PathVariable UUID profileId,
            @AuthenticationPrincipal User currentUser) {
        UserProfileDTO profile = userProfileService.getProfileById(profileId, currentUser);
        return ResponseEntity.ok(profile);
    }

    // UC3: Admin updates any profile
    @PutMapping("/{profileId}")
    @Operation(summary = "Update profile by ID", description = "Admin or owner updates profile")
    public ResponseEntity<UserProfileDTO> updateProfile(
            @PathVariable UUID profileId,
            @Valid @RequestBody UserProfileDTO profileDTO,
            @AuthenticationPrincipal User currentUser) {
        UserProfileDTO updatedProfile = userProfileService.updateProfile(profileId, profileDTO, currentUser);
        return ResponseEntity.ok(updatedProfile);
    }

    // UC3: Admin deletes any profile
    @DeleteMapping("/{profileId}")
    @Operation(summary = "Delete profile by ID", description = "Admin or owner deletes profile")
    public ResponseEntity<Void> deleteProfile(
            @PathVariable UUID profileId,
            @AuthenticationPrincipal User currentUser) {
        userProfileService.deleteProfile(profileId, currentUser);
        return ResponseEntity.noContent().build();
    }

    // UC4: Admin search/filter profiles
    @GetMapping
    @Operation(summary = "Search profiles", description = "Admin searches and filters profiles with pagination")
    public ResponseEntity<Page<UserProfileDTO>> searchProfiles(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            Pageable pageable,
            @AuthenticationPrincipal User currentUser) {
        Page<UserProfileDTO> profiles = userProfileService.searchProfiles(
                search, address, minAge, maxAge, pageable, currentUser);
        return ResponseEntity.ok(profiles);
    }
}