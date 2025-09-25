package com.example.demo.domain.userprofile;

import com.example.demo.core.security.permissionevaluators.UserPermissionEvaluator;
import com.example.demo.domain.user.User;
import com.example.demo.domain.user.UserDetailsImpl;
import com.example.demo.domain.userprofile.dto.UserProfileDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller für UserProfile Management
 *
 * Use Cases:
 * UC1: User erstellt eigenes Profil (Adresse, Geburtsdatum, Profilbild-URL und Alter)
 * UC2: User liest, aktualisiert oder löscht sein eigenes Profil
 * UC3: Administrator kann jedes Profil lesen, aktualisieren oder löschen
 * UC4: Administrator sucht, filtert und sortiert UserProfiles (mit Pagination)
 * UC5: Zugriffsschutz – Nur Besitzer oder Administrator dürfen auf ein bestimmtes Profil zugreifen
 */
@RestController
@RequestMapping("profiles")
@Tag(name = "User Profile", description = "User Profile management")
@SecurityRequirement(name = "bearerAuth")
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final UserPermissionEvaluator permissionEvaluator;

    public UserProfileController(UserProfileService userProfileService,
                                 UserPermissionEvaluator permissionEvaluator) {
        this.userProfileService = userProfileService;
        this.permissionEvaluator = permissionEvaluator;
    }

    // ---- UC1: User erstellt eigenes Profil ----
    @PostMapping
    @PreAuthorize("@userPermissionEvaluator.canCreateProfile(authentication.principal.user)")
    @Operation(summary = "Create own profile",
            description = "User erstellt eigenes Profil mit Adresse, Geburtsdatum, Profilbild-URL und Alter")
    public ResponseEntity<UserProfileDTO> createProfile(
            @Valid @RequestBody UserProfileDTO.CreateUpdateDTO createDTO,
            Authentication auth) {
        User currentUser = ((UserDetailsImpl) auth.getPrincipal()).user();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userProfileService.createProfile(createDTO, currentUser));
    }

    // ---- UC2: User liest eigenes Profil ----
    @GetMapping("/me")
    @PreAuthorize("@userPermissionEvaluator.canAccessOwnProfile(authentication.principal.user)")
    @Operation(summary = "Get own profile", description = "User liest eigenes Profil")
    public ResponseEntity<UserProfileDTO> getOwnProfile(Authentication auth) {
        User currentUser = ((UserDetailsImpl) auth.getPrincipal()).user();
        return ResponseEntity.ok(userProfileService.getOwnProfile(currentUser));
    }

    // ---- UC2: User aktualisiert eigenes Profil ----
    @PutMapping("/me")
    @PreAuthorize("@userPermissionEvaluator.canModifyOwnProfile(authentication.principal.user)")
    @Operation(summary = "Update own profile", description = "User aktualisiert eigenes Profil")
    public ResponseEntity<UserProfileDTO> updateOwnProfile(
            @Valid @RequestBody UserProfileDTO.CreateUpdateDTO updateDTO,
            Authentication auth) {
        User currentUser = ((UserDetailsImpl) auth.getPrincipal()).user();
        return ResponseEntity.ok(userProfileService.updateOwnProfile(updateDTO, currentUser));
    }

    // ---- UC2: User löscht eigenes Profil ----
    @DeleteMapping("/me")
    @PreAuthorize("@userPermissionEvaluator.canDeleteOwnProfile(authentication.principal.user)")
    @Operation(summary = "Delete own profile", description = "User löscht eigenes Profil")
    public ResponseEntity<Void> deleteOwnProfile(Authentication auth) {
        User currentUser = ((UserDetailsImpl) auth.getPrincipal()).user();
        userProfileService.deleteOwnProfile(currentUser);
        return ResponseEntity.noContent().build();
    }

    // ---- UC3 + UC5: Admin oder Owner liest Profil ----
    @GetMapping("/{profileId}")
    @PreAuthorize("@userPermissionEvaluator.canAccessProfile(authentication.principal.user, #profileId)")
    @Operation(summary = "Get profile by ID",
            description = "Admin oder Besitzer liest Profil anhand der Profil-ID")
    public ResponseEntity<UserProfileDTO> getProfileById(
            @PathVariable UUID profileId,
            Authentication auth) {
        User currentUser = ((UserDetailsImpl) auth.getPrincipal()).user();
        return ResponseEntity.ok(userProfileService.getProfileById(profileId, currentUser));
    }

    // ---- UC3 + UC5: Admin oder Owner aktualisiert Profil ----
    @PutMapping("/{profileId}")
    @PreAuthorize("@userPermissionEvaluator.canModifyProfile(authentication.principal.user, #profileId)")
    @Operation(summary = "Update profile by ID",
            description = "Admin oder Besitzer aktualisiert Profil")
    public ResponseEntity<UserProfileDTO> updateProfile(
            @PathVariable UUID profileId,
            @Valid @RequestBody UserProfileDTO.CreateUpdateDTO updateDTO,
            Authentication auth) {
        User currentUser = ((UserDetailsImpl) auth.getPrincipal()).user();
        return ResponseEntity.ok(userProfileService.updateProfile(profileId, updateDTO, currentUser));
    }

    // ---- UC3 + UC5: Admin oder Owner löscht Profil ----
    @DeleteMapping("/{profileId}")
    @PreAuthorize("@userPermissionEvaluator.canModifyProfile(authentication.principal.user, #profileId)")
    @Operation(summary = "Delete profile by ID",
            description = "Admin oder Besitzer löscht Profil")
    public ResponseEntity<Void> deleteProfile(
            @PathVariable UUID profileId,
            Authentication auth) {
        User currentUser = ((UserDetailsImpl) auth.getPrincipal()).user();
        userProfileService.deleteProfile(profileId, currentUser);
        return ResponseEntity.noContent().build();
    }

    // ---- UC4: Admin sucht/filtert/sortiert Profile ----
    @GetMapping
    @PreAuthorize("@userPermissionEvaluator.canSearchProfiles(authentication.principal.user)")
    @Operation(summary = "Search profiles",
            description = "Admin sucht, filtert und sortiert UserProfiles (mit Pagination)")
    public ResponseEntity<Page<UserProfileDTO>> searchProfiles(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            Pageable pageable,
            Authentication auth) {
        User currentUser = ((UserDetailsImpl) auth.getPrincipal()).user();
        return ResponseEntity.ok(
                userProfileService.searchProfiles(search, address, minAge, maxAge, pageable, currentUser));
    }
}