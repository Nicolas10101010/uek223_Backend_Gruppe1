package com.example.demo.core.security.permissionevaluators;

import com.example.demo.domain.role.Role;
import com.example.demo.domain.user.User;
import com.example.demo.domain.userprofile.UserProfileRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserPermissionEvaluator {

    private final UserProfileRepository userProfileRepository;

    public UserPermissionEvaluator(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    // ---- UC1: Create Profile ----
    public boolean canCreateProfile(User principal) {
        return principal != null && hasUserRole(principal);
    }

    // ---- UC2: Own Profile Operations ----
    public boolean canAccessOwnProfile(User principal) {
        return principal != null && hasUserRole(principal);
    }

    public boolean canModifyOwnProfile(User principal) {
        return principal != null && hasUserRole(principal);
    }

    public boolean canDeleteOwnProfile(User principal) {
        return principal != null && hasUserRole(principal);
    }

    // ---- UC3 + UC5: Profile by ID Operations ----
    public boolean canAccessProfile(User principal, UUID profileId) {
        if (isAdmin(principal)) {
            return true;
        }

        return userProfileRepository.findById(profileId)
                .map(profile -> profile.isOwnedBy(principal))
                .orElse(false);
    }

    public boolean canModifyProfile(User principal, UUID profileId) {
        return canAccessProfile(principal, profileId);
    }

    // ---- UC4: Search Profiles ----
    public boolean canSearchProfiles(User principal) {
        return isAdmin(principal);
    }

    // ---- Helper Methods ----
    private boolean hasUserRole(User principal) {
        if (principal == null) return false;

        return principal.getRoles().stream()
                .map(Role::getName)
                .anyMatch(name -> name.equals("ROLE_USER") || name.equals("USER"));
    }

    private boolean isAdmin(User principal) {
        if (principal == null) return false;

        return principal.getRoles().stream()
                .map(Role::getName)
                .anyMatch(name -> name.equals("ADMIN"));
    }
}