package com.example.demo.domain.userprofile.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record UserProfileRegisterDTO(

        @NotBlank(message = "Address is required")
        String address,

        @NotNull(message = "Birthdate is required")
        @Past(message = "Birthdate must be in the past")
        LocalDate birthdate,

        String profileImgUrl,

        @Positive(message = "Age must be positive")
        Integer age
) {}