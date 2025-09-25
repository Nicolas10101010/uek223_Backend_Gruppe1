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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * UserProfileServiceImpl - Business Logic Implementation für UserProfile
 *
 * Use Cases:
 * UC1: User erstellt eigenes Profil (Adresse, Geburtsdatum, Profilbild-URL und Alter)
 * UC2: User liest, aktualisiert oder löscht eigenes Profil
 * UC3: Administrator liest, aktualisiert oder löscht beliebige UserProfile
 * UC4: Administrator sucht, filtert und sortiert UserProfiles (mit Pagination)
 * UC5: Zugriffsschutz wird über @PreAuthorize im Controller gewährleistet
 *
 * Komponenten:
 * - Transaktionale Service-Schicht mit @Transactional
 * - Umfassendes Logging für alle Use Cases
 * - Partial Updates mit Null-Checks
 * - MapStruct Mapper für Entity/DTO-Konvertierung
 * - Reine Business-Logik ohne Security-Checks (diese erfolgen im Controller)
 */
@Service
@Transactional
public class UserProfileServiceImpl implements UserProfileService {

    private static final Logger logger = LogManager.getLogger(UserProfileServiceImpl.class);
    private static final String PROFILE_NOT_FOUND = "Profile not found";

    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;

    @Autowired
    public UserProfileServiceImpl(UserProfileRepository repo,
                                  @Qualifier("userProfileMapperImpl") UserProfileMapper mapper) {
        this.userProfileRepository = repo;
        this.userProfileMapper = mapper;
    }

    // ---- UC1: User erstellt eigenes Profil ----
    @Override
    public UserProfileDTO createProfile(UserProfileDTO.CreateUpdateDTO dto, User currentUser) {
        logger.info("UC1: User {} is creating a profile with data={}", currentUser.getEmail(), dto);

        // Business Rule: Ein User kann nur ein Profil haben
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

    // ---- UC2: User liest eigenes Profil ----
    @Override
    public UserProfileDTO getOwnProfile(User currentUser) {
        logger.info("UC2: User {} retrieving own profile", currentUser.getEmail());

        UserProfile profile = userProfileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> {
                    logger.warn("UC2: Profile not found for {}", currentUser.getEmail());
                    return new RuntimeException(PROFILE_NOT_FOUND);
                });

        return userProfileMapper.toDTO(profile);
    }

    // ---- UC2: User aktualisiert eigenes Profil ----
    @Override
    public UserProfileDTO updateOwnProfile(UserProfileDTO.CreateUpdateDTO dto, User currentUser) {
        logger.info("UC2: User {} updating their profile with data={}", currentUser.getEmail(), dto);

        UserProfile profile = userProfileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> {
                    logger.warn("UC2: Profile not found for {} during update", currentUser.getEmail());
                    return new RuntimeException(PROFILE_NOT_FOUND);
                });

        // Partial Update: nur nicht-null Werte aktualisieren
        if (dto.getAddress() != null) profile.setAddress(dto.getAddress());
        if (dto.getAge() != null) profile.setAge(dto.getAge());
        if (dto.getBirthdate() != null) profile.setBirthdate(dto.getBirthdate());
        if (dto.getProfileImgUrl() != null) profile.setProfileImgUrl(dto.getProfileImgUrl());

        UserProfile updated = userProfileRepository.save(profile);
        logger.info("UC2: Profile updated successfully (profileId={})", updated.getId());

        return userProfileMapper.toDTO(updated);
    }

    // ---- UC2: User löscht eigenes Profil ----
    @Override
    public void deleteOwnProfile(User currentUser) {
        logger.info("UC2: User {} deleting own profile", currentUser.getEmail());

        UserProfile profile = userProfileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> {
                    logger.warn("UC2: Profile not found for {} during deletion", currentUser.getEmail());
                    return new RuntimeException(PROFILE_NOT_FOUND);
                });

        userProfileRepository.delete(profile);
        logger.info("UC2: Profile {} deleted successfully for user {}", profile.getId(), currentUser.getEmail());
    }

    // ---- UC3: Admin oder Owner liest Profil (Security im Controller) ----
    @Override
    public UserProfileDTO getProfileById(UUID profileId, User currentUser) {
        logger.info("UC3: User {} retrieving profileId={}", currentUser.getEmail(), profileId);

        UserProfile profile = userProfileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException(PROFILE_NOT_FOUND));

        return userProfileMapper.toDTO(profile);
    }

    // ---- UC3: Admin oder Owner aktualisiert Profil (Security im Controller) ----
    @Override
    public UserProfileDTO updateProfile(UUID profileId, UserProfileDTO.CreateUpdateDTO dto, User currentUser) {
        logger.info("UC3: User {} updating profileId={} with data={}", currentUser.getEmail(), profileId, dto);

        UserProfile profile = userProfileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException(PROFILE_NOT_FOUND));

        // Partial Update: nur nicht-null Werte aktualisieren
        if (dto.getAddress() != null) profile.setAddress(dto.getAddress());
        if (dto.getAge() != null) profile.setAge(dto.getAge());
        if (dto.getBirthdate() != null) profile.setBirthdate(dto.getBirthdate());
        if (dto.getProfileImgUrl() != null) profile.setProfileImgUrl(dto.getProfileImgUrl());

        UserProfile updated = userProfileRepository.save(profile);
        logger.info("UC3: Profile {} updated successfully by {}", profileId, currentUser.getEmail());

        return userProfileMapper.toDTO(updated);
    }

    // ---- UC3: Admin oder Owner löscht Profil (Security im Controller) ----
    @Override
    public void deleteProfile(UUID profileId, User currentUser) {
        logger.info("UC3: User {} deleting profileId={}", currentUser.getEmail(), profileId);

        UserProfile profile = userProfileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException(PROFILE_NOT_FOUND));

        userProfileRepository.delete(profile);
        logger.info("UC3: Profile {} deleted successfully by {}", profileId, currentUser.getEmail());
    }

    // ---- UC4: Admin sucht, filtert und sortiert Profile (Security im Controller) ----
    @Override
    public Page<UserProfileDTO> searchProfiles(String searchTerm, String address,
                                               Integer minAge, Integer maxAge,
                                               Pageable pageable, User currentUser) {
        logger.info("UC4: User {} searching profiles with filters: search={}, address={}, minAge={}, maxAge={}",
                currentUser.getEmail(), searchTerm, address, minAge, maxAge);

        Page<UserProfile> profiles;

        if ((searchTerm != null && !searchTerm.isBlank()) ||
                (address != null && !address.isBlank()) ||
                minAge != null || maxAge != null) {

            // Verwende Repository-Methoden je nach verfügbaren Filtern
            if (searchTerm != null && !searchTerm.isBlank()) {
                profiles = userProfileRepository.searchProfiles(searchTerm, pageable);
            } else {
                profiles = userProfileRepository.findProfilesWithFilters(address, minAge, maxAge, pageable);
            }
        } else {
            profiles = userProfileRepository.findAll(pageable);
        }

        logger.info("UC4: Search completed by {} - Found {} profiles out of {} total",
                currentUser.getEmail(), profiles.getNumberOfElements(), profiles.getTotalElements());

        return profiles.map(userProfileMapper::toDTO);
    }

    // ---- UC4: Admin Liste aller Profile (Security im Controller) ----
    @Override
    public Page<UserProfileDTO> getAllProfiles(Pageable pageable, User currentUser) {
        logger.info("UC4: User {} retrieving all profiles", currentUser.getEmail());

        Page<UserProfile> profiles = userProfileRepository.findAll(pageable);

        logger.info("UC4: Retrieved {} profiles out of {} total for admin {}",
                profiles.getNumberOfElements(), profiles.getTotalElements(), currentUser.getEmail());

        return profiles.map(userProfileMapper::toDTO);
    }

    // ---- Helper-Methoden für zusätzliche Business-Logik ----
    @Override
    public boolean existsProfileForUser(User user) {
        return userProfileRepository.existsByUserId(user.getId());
    }

    @Override
    public UserProfileDTO getProfileByUserId(UUID userId, User currentUser) {
        logger.info("UC3: User {} retrieving profile for userId={}", currentUser.getEmail(), userId);

        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException(PROFILE_NOT_FOUND));

        return userProfileMapper.toDTO(profile);
    }
}