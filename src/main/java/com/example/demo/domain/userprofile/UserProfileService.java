package com.example.demo.domain.userprofile;

import com.example.demo.domain.user.User;
import com.example.demo.domain.userprofile.dto.UserProfileDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * UserProfileService - Business Logic Interface für UserProfile
 *
 * Use Cases:
 * UC1: User erstellt eigenes Profil (Adresse, Geburtsdatum, Profilbild-URL, Alter).
 * UC2: User liest, aktualisiert oder löscht sein eigenes Profil.
 * UC3: Administrator kann jedes Profil lesen, aktualisieren oder löschen.
 * UC4: Administrator sucht, filtert und sortiert UserProfiles (mit Pagination).
 * UC5: Zugriffsschutz – Nur Besitzer oder Admin dürfen auf ein bestimmtes Profil zugreifen.
 */

public interface UserProfileService {

    // UC1: User creates own profile
    UserProfileDTO createProfile(UserProfileDTO.CreateUpdateDTO createDTO, User currentUser);

    // UC2: User CRUD own profile
    UserProfileDTO getOwnProfile(User currentUser);
    UserProfileDTO updateOwnProfile(UserProfileDTO.CreateUpdateDTO updateDTO, User currentUser);
    void deleteOwnProfile(User currentUser);

    // UC3: Admin CRUD any profile
    UserProfileDTO getProfileById(UUID profileId, User currentUser);
    UserProfileDTO updateProfile(UUID profileId, UserProfileDTO.CreateUpdateDTO updateDTO, User currentUser);
    void deleteProfile(UUID profileId, User currentUser);

    // UC5: Admin search/filter with pagination
    Page<UserProfileDTO> searchProfiles(String searchTerm, String address,
                                        Integer minAge, Integer maxAge,
                                        Pageable pageable, User currentUser);
    Page<UserProfileDTO> getAllProfiles(Pageable pageable, User currentUser);

    // Helper-Methoden für interne Verwendung und andere Services
    boolean existsProfileForUser(User user);
    UserProfileDTO getProfileByUserId(UUID userId, User currentUser);
}