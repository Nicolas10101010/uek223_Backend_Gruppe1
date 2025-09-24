package com.example.demo.domain.userprofile;

import com.example.demo.domain.user.User;
import com.example.demo.domain.userprofile.dto.UserProfileCreateUpdateDTO;
import com.example.demo.domain.userprofile.dto.UserProfileDTO;
import com.example.demo.domain.userprofile.dto.UserProfileMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger logger = LogManager.getLogger(UserProfileServiceImpl.class);

    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;

    @Autowired
    public UserProfileServiceImpl(UserProfileRepository repo, UserProfileMapper mapper) {
        this.userProfileRepository = repo;
        this.userProfileMapper = mapper;
    }

    // -------------------------------
    // UC1: User creates own profile
    // -------------------------------
    @Override
    public UserProfileDTO createProfile(UserProfileCreateUpdateDTO dto, User currentUser) {
        logger.info("UC1: User {} is creating a profile with data={}", currentUser.getEmail(), dto);

        if (userProfileRepository.existsByUserId(currentUser.getId())) {
            logger.warn("UC1: Profile creation failed → User {} already has a profile", currentUser.getEmail());
            throw new IllegalStateException("User already has a profile");
        }

        UserProfile profile = userProfileMapper.toEntity(dto);
        profile.setUser(currentUser);
        UserProfile saved = userProfileRepository.save(profile);

        logger.info("UC1: Profile successfully created for {} with profileId={}", currentUser.getEmail(), saved.getId());
        return userProfileMapper.toDTO(saved);
    }

    // -------------------------------
    // UC2: Get own profile
    // -------------------------------
    @Override
    public UserProfileDTO getOwnProfile(User currentUser) {
        logger.info("UC2: User {} retrieving own profile", currentUser.getEmail());

        UserProfile profile = userProfileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> {
                    logger.warn("UC2: Profile not found for {}", currentUser.getEmail());
                    return new RuntimeException("Profile not found");
                });

        logger.info("UC2: Profile for {} retrieved successfully: profileId={}", currentUser.getEmail(), profile.getId());
        return userProfileMapper.toDTO(profile);
    }

    // -------------------------------
    // UC2: Update own profile
    // -------------------------------
    @Override
    public UserProfileDTO updateOwnProfile(UserProfileCreateUpdateDTO dto, User currentUser) {
        logger.info("UC2: User {} updating their profile with data={}", currentUser.getEmail(), dto);

        UserProfile profile = userProfileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> {
                    logger.warn("UC2: Profile not found for {} during update", currentUser.getEmail());
                    return new RuntimeException("Profile not found");
                });

        if (dto.getAddress() != null) profile.setAddress(dto.getAddress());
        if (dto.getAge() != null) profile.setAge(dto.getAge());

        UserProfile updated = userProfileRepository.save(profile);
        logger.info("UC2: Profile for {} updated successfully (profileId={})", currentUser.getEmail(), updated.getId());

        return userProfileMapper.toDTO(updated);
    }

    // -------------------------------
    // UC2: Delete own profile
    // -------------------------------
    @Override
    public void deleteOwnProfile(User currentUser) {
        logger.info("UC2: User {} deleting own profile", currentUser.getEmail());

        UserProfile profile = userProfileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> {
                    logger.warn("UC2: Profile not found for {} during deletion", currentUser.getEmail());
                    return new RuntimeException("Profile not found");
                });

        userProfileRepository.delete(profile);
        logger.info("UC2: Profile {} deleted successfully for user {}", profile.getId(), currentUser.getEmail());
    }

    // -------------------------------
    // UC3: Admin get profile by ID
    // -------------------------------
    @Override
    public UserProfileDTO getProfileById(UUID profileId, User currentUser) {
        logger.info("UC3: User {} attempting to retrieve profileId={}", currentUser.getEmail(), profileId);

        UserProfile profile = userProfileRepository.findById(profileId)
                .orElseThrow(() -> {
                    logger.warn("UC3: Profile {} not found (requested by {})", profileId, currentUser.getEmail());
                    return new RuntimeException("Profile not found");
                });

        if (!isAdminOrOwner(currentUser, profile)) {
            logger.warn("UC3: Access denied → {} tried to access profile {}", currentUser.getEmail(), profileId);
            throw new AccessDeniedException("Access denied");
        }

        logger.info("UC3: Profile {} successfully retrieved by {}", profileId, currentUser.getEmail());
        return userProfileMapper.toDTO(profile);
    }

    // -------------------------------
    // UC4: Admin update profile
    // -------------------------------
    @Override
    public UserProfileDTO updateProfile(UUID profileId, UserProfileCreateUpdateDTO dto, User currentUser) {
        logger.info("UC4: User {} attempting to update profileId={} with data={}", currentUser.getEmail(), profileId, dto);

        UserProfile profile = userProfileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        if (!isAdminOrOwner(currentUser, profile)) {
            logger.warn("UC4: Access denied → {} tried to update profile {}", currentUser.getEmail(), profileId);
            throw new AccessDeniedException("Access denied");
        }

        if (dto.getAddress() != null) profile.setAddress(dto.getAddress());
        if (dto.getAge() != null) profile.setAge(dto.getAge());

        UserProfile updated = userProfileRepository.save(profile);
        logger.info("UC4: Profile {} updated successfully by {}", profileId, currentUser.getEmail());

        return userProfileMapper.toDTO(updated);
    }

    // -------------------------------
    // UC4: Admin delete profile
    // -------------------------------
    @Override
    public void deleteProfile(UUID profileId, User currentUser) {
        logger.info("UC4: User {} attempting to delete profileId={}", currentUser.getEmail(), profileId);

        UserProfile profile = userProfileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        if (!isAdminOrOwner(currentUser, profile)) {
            logger.warn("UC4: Access denied → {} tried to delete profile {}", currentUser.getEmail(), profileId);
            throw new AccessDeniedException("Access denied");
        }

        userProfileRepository.delete(profile);
        logger.info("UC4: Profile {} deleted successfully by {}", profileId, currentUser.getEmail());
    }

    // -------------------------------
    // UC5: Admin search/filter profiles
    // -------------------------------
    @Override
    public Page<UserProfileDTO> searchProfiles(String searchTerm, String address,
                                               Integer minAge, Integer maxAge,
                                               Pageable pageable, User currentUser) {
        logger.info("UC5: User {} searching profiles (searchTerm='{}', address='{}', minAge={}, maxAge={}, page={}, size={})",
                currentUser.getEmail(), searchTerm, address, minAge, maxAge, pageable.getPageNumber(), pageable.getPageSize());

        if (!isAdmin(currentUser)) {
            logger.warn("UC5: Access denied → {} tried to search profiles", currentUser.getEmail());
            throw new AccessDeniedException("Admin only");
        }

        Page<UserProfile> profiles;
        if (searchTerm != null && !searchTerm.isBlank()) {
            profiles = userProfileRepository.searchProfiles(searchTerm, pageable);
        } else {
            profiles = userProfileRepository.findProfilesWithFilters(address, minAge, maxAge, pageable);
        }

        logger.info("UC5: User {} search complete → {} profiles found (total={})",
                currentUser.getEmail(), profiles.getNumberOfElements(), profiles.getTotalElements());

        return profiles.map(userProfileMapper::toDTO);
    }

    // -------------------------------
    // UC? getAllProfiles (Admin only)
    // -------------------------------
    @Override
    public Page<UserProfileDTO> getAllProfiles(Pageable pageable, User currentUser) {
        logger.info("Extra: User {} retrieving all profiles (page={}, size={})",
                currentUser.getEmail(), pageable.getPageNumber(), pageable.getPageSize());

        if (!isAdmin(currentUser)) {
            logger.warn("getAllProfiles: Access denied → {} is not admin", currentUser.getEmail());
            throw new AccessDeniedException("Admin only");
        }

        Page<UserProfile> profiles = userProfileRepository.findAll(pageable);
        logger.info("getAllProfiles: User {} retrieved {} profiles (total={})",
                currentUser.getEmail(), profiles.getNumberOfElements(), profiles.getTotalElements());

        return profiles.map(userProfileMapper::toDTO);
    }

    // -------------------------------
    // Helper methods
    // -------------------------------
    @Override
    public boolean existsProfileForUser(User user) {
        logger.info("Helper: Checking if profile exists for {}", user.getEmail());
        return userProfileRepository.existsByUserId(user.getId());
    }

    @Override
    public UserProfileDTO getProfileByUserId(UUID userId, User currentUser) {
        logger.info("Helper: User {} getting profile by userId={}", currentUser.getEmail(), userId);

        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        if (!isAdminOrOwner(currentUser, profile)) {
            logger.warn("Helper: Access denied → {} tried to access profile of userId={}", currentUser.getEmail(), userId);
            throw new AccessDeniedException("Access denied");
        }

        logger.info("Helper: Profile for userId={} retrieved successfully by {}", userId, currentUser.getEmail());
        return userProfileMapper.toDTO(profile);
    }

    // -------------------------------
    // Helpers
    // -------------------------------
    private boolean isAdminOrOwner(User currentUser, UserProfile profile) {
        return isAdmin(currentUser) || profile.getUser().getId().equals(currentUser.getId());
    }

    private boolean isAdmin(User currentUser) {
        return currentUser.getRoles().stream()
                .flatMap(role -> role.getAuthorities().stream())
                .anyMatch(a -> a.getName().equals("ADMIN"));
    }
}