package com.example.demo.domain.userprofile.dto;

import com.example.demo.core.generic.AbstractDTO;
import com.example.demo.domain.user.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.UUID;

/**
 * UserProfileDTO - Data Transfer Object für UserProfile
 *
 * Komponenten:
 * - Bean Validation für Eingabevalidierung
 * - Jackson Annotations für JSON-Serialisierung
 * - Nested CreateUpdateDTO für Create/Update-Operationen
 * - Fluent API durch Accessors(chain = true)
 */
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class UserProfileDTO extends AbstractDTO {

    private UserDTO user;

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "Birthdate is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;

    @Size(max = 1024)
    private String profileImgUrl;

    @NotNull(message = "Age is required")
    @Min(value = 0, message = "Age must be >= 0")
    @Max(value = 150, message = "Age must be <= 150")
    private Integer age;

    public UserProfileDTO(UUID profileId, UserDTO user, String address, LocalDate birthdate, String profileImgUrl, Integer age) {
        super(profileId);
        this.user = user;
        this.address = address;
        this.birthdate = birthdate;
        this.profileImgUrl = profileImgUrl;
        this.age = age;
    }

    /**
     * Nested DTO für Create/Update-Operationen
     * Enthält nur die veränderbaren Felder ohne User-Referenz
     */
    @NoArgsConstructor
    @Getter
    @Setter
    public static class CreateUpdateDTO {
        @NotBlank(message = "Address is required")
        private String address;

        @NotNull(message = "Birthdate is required")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate birthdate;

        @Size(max = 1024)
        private String profileImgUrl;

        @NotNull(message = "Age is required")
        @Min(value = 0, message = "Age must be >= 0")
        @Max(value = 150, message = "Age must be <= 150")
        private Integer age;

        /**
         * Konvertiert CreateUpdateDTO zu vollständigem UserProfileDTO
         * Wird hauptsächlich in Tests verwendet
         */
        public UserProfileDTO toUserProfileDTO(UserDTO userDTO) {
            return new UserProfileDTO()
                    .setUser(userDTO)
                    .setAddress(this.address)
                    .setBirthdate(this.birthdate)
                    .setProfileImgUrl(this.profileImgUrl)
                    .setAge(this.age);
        }
    }
}