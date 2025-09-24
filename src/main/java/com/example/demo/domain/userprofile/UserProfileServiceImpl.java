package com.example.demo.domain.userprofile;

import com.example.demo.domain.user.User;
import com.example.demo.domain.userprofile.dto.UserProfileDTO;
import com.example.demo.domain.userprofile.dto.UserProfileMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    public UserProfileServiceImpl(UserProfileRepository repo,
                                  @Qualifier("userProfileMapperImpl") UserProfileMapper mapper) {
        this.userProfileRepository = repo;
        this.userProfileMapper = mapper;
    }

    // -------------------------------
    // UC1: User creates own profile
    // -------------------------------
    @Override
    public UserProfileDTO createProfile(UserProfileDTO.CreateUpdateDTO dto, User currentUser) {
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

        return userProfileMapper.toDTO(profile);
    }

    // -------------------------------
    // UC2: Update own profile
    // -------------------------------
    @Override
    public UserProfileDTO updateOwnProfile(UserProfileDTO.CreateUpdateDTO dto, User currentUser) {
        logger.info("UC2: User {} updating their profile with data={}", currentUser.getEmail(), dto);

        UserProfile profile = userProfileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> {
                    logger.warn("UC2: Profile not found for {} during update", currentUser.getEmail());
                    return new RuntimeException("Profile not found");
                });

        if (dto.getAddress() != null) profile.setAddress(dto.getAddress());
        if (dto.getAge() != null) profile.setAge(dto.getAge());
        if (dto.getBirthdate() != null) profile.setBirthdate(dto.getBirthdate());
        if (dto.getProfileImgUrl() != null) profile.setProfileImgUrl(dto.getProfileImgUrl());

        UserProfile updated = userProfileRepository.save(profile);
        logger.info("UC2: Profile updated successfully (profileId={})", updated.getId());

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
        logger.info("UC3: User {} retrieving profileId={}", currentUser.getEmail(), profileId);

        UserProfile profile = userProfileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        if (!isAdminOrOwner(currentUser, profile)) {
            throw new AccessDeniedException("Access denied");
        }

        return userProfileMapper.toDTO(profile);
    }

    // -------------------------------
    // UC4: Admin update profile
    // -------------------------------
    @Override
    public UserProfileDTO updateProfile(UUID profileId, UserProfileDTO.CreateUpdateDTO dto, User currentUser) {
        logger.info("UC4: User {} updating profileId={} with data={}", currentUser.getEmail(), profileId, dto);

        UserProfile profile = userProfileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        if (!isAdminOrOwner(currentUser, profile)) {
            throw new AccessDeniedException("Access denied");
        }

        if (dto.getAddress() != null) profile.setAddress(dto.getAddress());
        if (dto.getAge() != null) profile.setAge(dto.getAge());
        if (dto.getBirthdate() != null) profile.setBirthdate(dto.getBirthdate());
        if (dto.getProfileImgUrl() != null) profile.setProfileImgUrl(dto.getProfileImgUrl());

        UserProfile updated = userProfileRepository.save(profile);
        return userProfileMapper.toDTO(updated);
    }

    // -------------------------------
    // UC4: Admin delete profile
    // -------------------------------
    @Override
    public void deleteProfile(UUID profileId, User currentUser) {
        logger.info("UC4: User {} deleting profileId={}", currentUser.getEmail(), profileId);

        UserProfile profile = userProfileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        if (!isAdminOrOwner(currentUser, profile)) {
            throw new AccessDeniedException("Access denied");
        }

        userProfileRepository.delete(profile);
    }

    @Override
    public Page<UserProfileDTO> searchProfiles(String searchTerm, String address,
                                               Integer minAge, Integer maxAge,
                                               Pageable pageable, User currentUser) {
        logger.info("UC5: User {} searching profiles", currentUser.getEmail());

        if (!isAdmin(currentUser)) {
            throw new AccessDeniedException("Admin only");
        }

        Page<UserProfile> profiles = (searchTerm != null && !searchTerm.isBlank())
                ? userProfileRepository.searchProfiles(searchTerm, pageable)
                : userProfileRepository.findProfilesWithFilters(address, minAge, maxAge, pageable);

        return profiles.map(userProfileMapper::toDTO);
    }

    @Override
    public Page<UserProfileDTO> getAllProfiles(Pageable pageable, User currentUser) {
        if (!isAdmin(currentUser)) {
            throw new AccessDeniedException("Admin only");
        }

        return userProfileRepository.findAll(pageable).map(userProfileMapper::toDTO);
    }

    @Override
    public boolean existsProfileForUser(User user) {
        return userProfileRepository.existsByUserId(user.getId());
    }

    @Override
    public UserProfileDTO getProfileByUserId(UUID userId, User currentUser) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        if (!isAdminOrOwner(currentUser, profile)) {
            throw new AccessDeniedException("Access denied");
        }

        return userProfileMapper.toDTO(profile);
    }

    private boolean isAdminOrOwner(User currentUser, UserProfile profile) {
        return isAdmin(currentUser) || profile.getUser().getId().equals(currentUser.getId());
    }

    private boolean isAdmin(User currentUser) {
        return currentUser.getRoles().stream()
                .anyMatch(role -> "ADMIN".equals(role.getName()));
    }
}
