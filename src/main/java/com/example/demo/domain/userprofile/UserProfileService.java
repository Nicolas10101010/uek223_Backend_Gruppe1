package com.example.demo.domain.userprofile;

import com.example.demo.domain.user.User;
import com.example.demo.domain.userprofile.dto.UserProfileCreateUpdateDTO;
import com.example.demo.domain.userprofile.dto.UserProfileDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserProfileService {

    // UC1: User creates own profile
    UserProfileDTO createProfile(UserProfileCreateUpdateDTO registerDTO, User currentUser);

    // UC2: User CRUD own profile
    UserProfileDTO getOwnProfile(User currentUser);
    UserProfileDTO updateOwnProfile(UserProfileCreateUpdateDTO profileDTO, User currentUser);
    void deleteOwnProfile(User currentUser);

    // UC3: Admin CRUD any profile
    UserProfileDTO getProfileById(UUID profileId, User currentUser);
    UserProfileDTO updateProfile(UUID profileId, UserProfileCreateUpdateDTO createProfileDTO, User currentUser);
    void deleteProfile(UUID profileId, User currentUser);

    // UC4: Admin search/filter with pagination
    Page<UserProfileDTO> searchProfiles(String searchTerm, String address,
                                        Integer minAge, Integer maxAge,
                                        Pageable pageable, User currentUser);
    Page<UserProfileDTO> getAllProfiles(Pageable pageable, User currentUser);

    // Helper methods
    boolean existsProfileForUser(User user);
    UserProfileDTO getProfileByUserId(UUID userId, User currentUser);
}