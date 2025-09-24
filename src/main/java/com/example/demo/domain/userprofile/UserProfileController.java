package com.example.demo.domain.userprofile;

import com.example.demo.domain.user.User;
import com.example.demo.domain.user.UserDetailsImpl;
import com.example.demo.domain.userprofile.dto.UserProfileCreateUpdateDTO;
import com.example.demo.domain.userprofile.dto.UserProfileDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("profiles")
@Tag(name = "User Profile", description = "User Profile management")
@SecurityRequirement(name = "bearerAuth")
public class UserProfileController {

    private static final Logger logger = LogManager.getLogger(UserProfileController.class);

    private final UserProfileService userProfileService;

    public UserProfileController(@Autowired UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    // UC1: Create own profile
    @PostMapping
    @Operation(summary = "Create user profile", description = "User creates their own profile")
    public ResponseEntity<UserProfileDTO> createProfile(
            @Valid @RequestBody UserProfileCreateUpdateDTO registerDTO, Authentication auth) {
            User currentUser = ((UserDetailsImpl) auth.getPrincipal()).user();
        logger.info("UC1-API: POST /profiles - User {} creating profile", currentUser.getEmail());

        try {
            UserProfileDTO createdProfile = userProfileService.createProfile(registerDTO, currentUser);

            logger.info("UC1-API: Profile created successfully via API for user {} with profileId={}",
                    currentUser.getEmail(), createdProfile.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(createdProfile);
        } catch (Exception e) {
            logger.error("UC1-API: Profile creation failed via API for user {} - Error: {}",
                    currentUser.getEmail(), e.getMessage(), e);
            throw e;
        }
    }

    // UC2: Get own profile
    @GetMapping("/me")
    @Operation(summary = "Get own profile", description = "User gets their own profile")
    public ResponseEntity<UserProfileDTO> getOwnProfile(Authentication auth) {
        User currentUser = ((UserDetailsImpl) auth.getPrincipal()).user();
        logger.info("UC2-API: GET profiles/me - User {} retrieving own profile", currentUser.getEmail());

        try {
            UserProfileDTO profile = userProfileService.getOwnProfile(currentUser);

            logger.info("UC2-API: Own profile retrieved successfully via API for user {} with profileId={}",
                    currentUser.getEmail(), profile.getId());

            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            logger.error("UC2-API: Failed to retrieve own profile via API for user {} - Error: {}",
                    currentUser.getEmail(), e.getMessage(), e);
            throw e;
        }
    }

    // UC2: Update own profile
    @PutMapping("/me")
    @Operation(summary = "Update own profile", description = "User updates their own profile")
    public ResponseEntity<UserProfileDTO> updateOwnProfile(
            @Valid @RequestBody UserProfileCreateUpdateDTO profileDTO,
            Authentication auth) {
        User currentUser = ((UserDetailsImpl) auth.getPrincipal()).user();

        logger.info("UC2-API: PUT /api/profiles/me - User {} updating own profile", currentUser.getEmail());

        try {
            UserProfileDTO updatedProfile = userProfileService.updateOwnProfile(profileDTO, currentUser);

            logger.info("UC2-API: Own profile updated successfully via API for user {} with profileId={}",
                    currentUser.getEmail(), updatedProfile.getId());

            return ResponseEntity.ok(updatedProfile);
        } catch (Exception e) {
            logger.error("UC2-API: Failed to update own profile via API for user {} - Error: {}",
                    currentUser.getEmail(), e.getMessage(), e);
            throw e;
        }
    }

    // UC2: Delete own profile
    @DeleteMapping("/me")
    @Operation(summary = "Delete own profile", description = "User deletes their own profile")
    public ResponseEntity<Void> deleteOwnProfile(Authentication auth) {
        User currentUser = ((UserDetailsImpl) auth.getPrincipal()).user();
        logger.info("UC2-API: DELETE /api/profiles/me - User {} deleting own profile", currentUser.getEmail());

        try {
            userProfileService.deleteOwnProfile(currentUser);

            logger.info("UC2-API: Own profile deleted successfully via API for user {}", currentUser.getEmail());

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("UC2-API: Failed to delete own profile via API for user {} - Error: {}",
                    currentUser.getEmail(), e.getMessage(), e);
            throw e;
        }
    }

    // UC3: Admin gets any profile by ID
    @GetMapping("/{profileId}")
    @Operation(summary = "Get profile by ID", description = "Admin or owner gets profile by ID")
    public ResponseEntity<UserProfileDTO> getProfileById(
            @PathVariable UUID profileId,
            Authentication auth) {
        User currentUser = ((UserDetailsImpl) auth.getPrincipal()).user();
        logger.info("UC3-API: GET /api/profiles/{} - User {} retrieving profile", profileId, currentUser.getEmail());

        try {
            UserProfileDTO profile = userProfileService.getProfileById(profileId, currentUser);

            logger.info("UC3-API: Profile profileId={} retrieved successfully via API by user {}",
                    profileId, currentUser.getEmail());

            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            logger.error("UC3-API: Failed to retrieve profileId={} via API by user {} - Error: {}",
                    profileId, currentUser.getEmail(), e.getMessage(), e);
            throw e;
        }
    }

    // UC4: Admin updates any profile
    @PutMapping("/{profileId}")
    @Operation(summary = "Update profile by ID", description = "Admin or owner updates profile")
    public ResponseEntity<UserProfileDTO> updateProfile(
            @PathVariable UUID profileId,
            @Valid @RequestBody UserProfileCreateUpdateDTO profileDTO,
            Authentication auth) {
        User currentUser = ((UserDetailsImpl) auth.getPrincipal()).user();

        logger.info("UC4-API: PUT /api/profiles/{} - User {} updating profile", profileId, currentUser.getEmail());

        try {
            UserProfileDTO updatedProfile = userProfileService.updateProfile(profileId, profileDTO, currentUser);

            logger.info("UC4-API: Profile profileId={} updated successfully via API by user {}",
                    profileId, currentUser.getEmail());

            return ResponseEntity.ok(updatedProfile);
        } catch (Exception e) {
            logger.error("UC4-API: Failed to update profileId={} via API by user {} - Error: {}",
                    profileId, currentUser.getEmail(), e.getMessage(), e);
            throw e;
        }
    }

    // UC4: Admin deletes any profile
    @DeleteMapping("/{profileId}")
    @Operation(summary = "Delete profile by ID", description = "Admin or owner deletes profile")
    public ResponseEntity<Void> deleteProfile(
            @PathVariable UUID profileId,
            Authentication auth) {
        User currentUser = ((UserDetailsImpl) auth.getPrincipal()).user();

        logger.info("UC4-API: DELETE /api/profiles/{} - User {} deleting profile", profileId, currentUser.getEmail());

        try {
            userProfileService.deleteProfile(profileId, currentUser);

            logger.info("UC4-API: Profile profileId={} deleted successfully via API by user {}",
                    profileId, currentUser.getEmail());

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("UC4-API: Failed to delete profileId={} via API by user {} - Error: {}",
                    profileId, currentUser.getEmail(), e.getMessage(), e);
            throw e;
        }
    }

    // UC5: Admin search/filter profiles
    @GetMapping
    @Operation(summary = "Search profiles", description = "Admin searches and filters profiles with pagination")
    public ResponseEntity<Page<UserProfileDTO>> searchProfiles(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            Pageable pageable,
            Authentication auth) {
        User currentUser = ((UserDetailsImpl) auth.getPrincipal()).user();

        logger.info("UC5-API: GET /api/profiles - User {} searching profiles with filters: search='{}', address='{}', minAge={}, maxAge={}, page={}, size={}",
                currentUser.getEmail(), search, address, minAge, maxAge,
                pageable.getPageNumber(), pageable.getPageSize());

        try {
            Page<UserProfileDTO> profiles = userProfileService.searchProfiles(
                    search, address, minAge, maxAge, pageable, currentUser);

            logger.info("UC5-API: Profile search completed via API by user {} - Found {} profiles out of {} total",
                    currentUser.getEmail(), profiles.getNumberOfElements(), profiles.getTotalElements());

            return ResponseEntity.ok(profiles);
        } catch (Exception e) {
            logger.error("UC5-API: Profile search failed via API for user {} - Error: {}",
                    currentUser.getEmail(), e.getMessage(), e);
            throw e;
        }
    }
}