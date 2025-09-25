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
import java.time.Period;
import java.util.UUID;

/**
 * UserProfileDTO - Data Transfer Object für UserProfile
 *
 * Komponenten:
 * - Bean Validation für Eingabevalidierung
 * - Custom Validation via @AssertTrue für Altersprüfung
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
    @Past(message = "Birthdate must be in the past")
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
     * Custom Validierung: Age muss zum Birthdate passen.
     */
    @AssertTrue(message = "Age does not match the birthdate")
    public boolean isAgeConsistent() {
        if (birthdate == null || age == null) return true; // andere Validatoren kümmern sich um @NotNull
        int calculated = Period.between(birthdate, LocalDate.now()).getYears();
        return calculated == age;
    }

    /**
     * Nested DTO für Create/Update-Operationen
     * Enthält nur veränderbare Felder
     */
    @NoArgsConstructor
    @Getter
    @Setter
    public static class CreateUpdateDTO {
        @NotBlank(message = "Address is required")
        private String address;

        @NotNull(message = "Birthdate is required")
        @Past(message = "Birthdate must be in the past")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate birthdate;

        @Size(max = 1024)
        private String profileImgUrl;

        @NotNull(message = "Age is required")
        @Min(value = 0, message = "Age must be >= 0")
        @Max(value = 150, message = "Age must be <= 150")
        private Integer age;

        @AssertTrue(message = "Age does not match the birthdate")
        public boolean isAgeConsistent() {
            if (birthdate == null || age == null) return true;
            int calculated = Period.between(birthdate, LocalDate.now()).getYears();
            return calculated == age;
        }

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