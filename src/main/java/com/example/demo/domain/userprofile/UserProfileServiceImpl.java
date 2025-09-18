package com.example.demo.domain.userprofile;

import com.example.demo.domain.user.User;
import com.example.demo.domain.userprofile.dto.UserProfileCreateUpdateDTO;
import com.example.demo.domain.userprofile.dto.UserProfileDTO;
import com.example.demo.domain.userprofile.dto.UserProfileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;

    private final UserProfileMapper userProfileMapper;

    public UserProfileServiceImpl(@Autowired UserProfileRepository userProfileRepository,
                                  @Autowired UserProfileMapper userProfileMapper) {
        this.userProfileRepository = userProfileRepository;
        this.userProfileMapper = userProfileMapper;
    }

    // UC1: User creates own profile
    @Override
    public UserProfileDTO createProfile(UserProfileCreateUpdateDTO registerDTO, User currentUser) {
        // Check if user already has a profile
        if (userProfileRepository.existsByUserId(currentUser.getId())) {
            throw new IllegalStateException("User already has a profile");
        }

        // Create new profile using mapper
        UserProfile profile = userProfileMapper.toEntity(registerDTO);
        profile.setUser(currentUser);

        UserProfile savedProfile = userProfileRepository.save(profile);
        return userProfileMapper.toDTO(savedProfile);
    }

    // UC2: User reads own profile
    @Override
    public UserProfileDTO getOwnProfile(User currentUser) {
        UserProfile profile = userProfileRepository.findByUser(currentUser)
                .orElseThrow(() -> new RuntimeException("Profile not found for user: " + currentUser.getEmail()));
        return userProfileMapper.toDTO(profile);
    }

    // UC2: User updates own profile
    @Override
    public UserProfileDTO updateOwnProfile(UserProfileCreateUpdateDTO profileDTO, User currentUser) {
        UserProfile existingProfile = userProfileRepository.findByUser(currentUser)
                .orElseThrow(() -> new RuntimeException("Profile not found for user: " + currentUser.getEmail()));

        // Update fields
        userProfileMapper.updateEntityFromDTO(profileDTO, existingProfile);
        UserProfile savedProfile = userProfileRepository.save(existingProfile);
        return userProfileMapper.toDTO(savedProfile);
    }

    // UC2: User deletes own profile
    @Override
    public void deleteOwnProfile(User currentUser) {
        UserProfile profile = userProfileRepository.findByUser(currentUser)
                .orElseThrow(() -> new RuntimeException("Profile not found for user: " + currentUser.getEmail()));
        userProfileRepository.delete(profile);
    }

    // UC3: Admin reads any profile by ID
    @Override
    public UserProfileDTO getProfileById(UUID profileId, User currentUser) {
        UserProfile profile = userProfileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found with ID: " + profileId));

        // UC5: Access control - only owner or admin
        if (!profile.isOwnedBy(currentUser) && !isAdmin(currentUser)) {
            throw new AccessDeniedException("Access denied: You can only access your own profile");
        }

        return userProfileMapper.toDTO(profile);
    }

    // UC3: Admin updates any profile
    @Override
    public UserProfileDTO updateProfile(UUID profileId, UserProfileCreateUpdateDTO profileDTO, User currentUser) {
        UserProfile existingProfile = userProfileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found with ID: " + profileId));

        // UC5: Access control - only owner or admin
        if (!existingProfile.isOwnedBy(currentUser) && !isAdmin(currentUser)) {
            throw new AccessDeniedException("Access denied: You can only update your own profile");
        }

        userProfileMapper.updateEntityFromDTO(profileDTO, existingProfile);
        UserProfile savedProfile = userProfileRepository.save(existingProfile);
        return userProfileMapper.toDTO(savedProfile);
    }

    // UC3: Admin deletes any profile
    @Override
    public void deleteProfile(UUID profileId, User currentUser) {
        UserProfile profile = userProfileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found with ID: " + profileId));

        // UC5: Access control - only owner or admin
        if (!profile.isOwnedBy(currentUser) && !isAdmin(currentUser)) {
            throw new AccessDeniedException("Access denied: You can only delete your own profile");
        }

        userProfileRepository.delete(profile);
    }

    // UC4: Admin search/filter profiles with pagination
    @Override
    public Page<UserProfileDTO> searchProfiles(String searchTerm, String address,
                                               Integer minAge, Integer maxAge,
                                               Pageable pageable, User currentUser) {
        // Only admins can search all profiles
        if (!isAdmin(currentUser)) {
            throw new AccessDeniedException("Access denied: Only administrators can search profiles");
        }

        Page<UserProfile> profiles;

        // If search term is provided, use text search
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            profiles = userProfileRepository.searchProfiles(searchTerm.trim(), pageable);
        } else {
            // Otherwise use filter search
            profiles = userProfileRepository.findProfilesWithFilters(address, minAge, maxAge, pageable);
        }

        return profiles.map(userProfileMapper::toDTO);
    }

    // UC4: Admin gets all profiles with pagination
    @Override
    public Page<UserProfileDTO> getAllProfiles(Pageable pageable, User currentUser) {
        // Only admins can view all profiles
        if (!isAdmin(currentUser)) {
            throw new AccessDeniedException("Access denied: Only administrators can view all profiles");
        }

        return userProfileRepository.findAll(pageable)
                .map(userProfileMapper::toDTO);
    }

    // Helper method: Check if profile exists for user
    @Override
    public boolean existsProfileForUser(User user) {
        return userProfileRepository.existsByUserId(user.getId());
    }

    // Helper method: Get profile by user ID (with access control)
    @Override
    public UserProfileDTO getProfileByUserId(UUID userId, User currentUser) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found for user ID: " + userId));

        // UC5: Access control - only owner or admin
        if (!profile.isOwnedBy(currentUser) && !isAdmin(currentUser)) {
            throw new AccessDeniedException("Access denied: You can only access your own profile");
        }

        return userProfileMapper.toDTO(profile);
    }

    // UC5: Access control helper - check if user is admin
    private boolean isAdmin(User user) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ADMIN") || role.getName().equals("ROLE_ADMIN"));
    }
}