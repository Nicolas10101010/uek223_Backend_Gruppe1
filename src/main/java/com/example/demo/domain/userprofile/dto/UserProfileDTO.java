package com.example.demo.domain.userprofile.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.UUID;

public record UserProfileDTO(
        UUID id,

        @NotBlank(message = "Address is required")
        String address,

        @NotNull(message = "Birthdate is required")
        @Past(message = "Birthdate must be in the past")
        LocalDate birthdate,

        String profileImgUrl,

        @Positive(message = "Age must be positive")
        Integer age,

        // User information for responses
        UUID userId,
        String userFirstName,
        String userLastName,
        String userEmail
) {

    // Constructor for creating profile (without user details and id)
    public UserProfileDTO(String address, LocalDate birthdate, String profileImgUrl, Integer age) {
        this(null, address, birthdate, profileImgUrl, age, null, null, null, null);
    }

    // Constructor for updating profile (with id but without user details)
    public UserProfileDTO(UUID id, String address, LocalDate birthdate, String profileImgUrl, Integer age) {
        this(id, address, birthdate, profileImgUrl, age, null, null, null, null);
    }
}