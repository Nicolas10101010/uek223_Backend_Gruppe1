package com.example.demo.domain.userprofile.dto;

import com.example.demo.core.generic.AbstractDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class UserProfileDTO extends AbstractDTO {

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

    private UUID userId;
    private String userFirstName;
    private String userLastName;
    private String userEmail;

    public UserProfileDTO(UUID id,
                          String address,
                          LocalDate birthdate,
                          String profileImgUrl,
                          Integer age,
                          UUID userId,
                          String userFirstName,
                          String userLastName,
                          String userEmail) {
        super(id);
        this.address = address;
        this.birthdate = birthdate;
        this.profileImgUrl = profileImgUrl;
        this.age = age;
        this.userId = userId;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmail = userEmail;
    }

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
        private String profileImgUrl; // optional

        @NotNull(message = "Age is required")
        @Min(value = 0, message = "Age must be >= 0")
        @Max(value = 150, message = "Age must be <= 150")
        private Integer age;

        public CreateUpdateDTO(String address,
                               LocalDate birthdate,
                               String profileImgUrl,
                               Integer age) {
            this.address = address;
            this.birthdate = birthdate;
            this.profileImgUrl = profileImgUrl;
            this.age = age;
        }

        public UserProfileDTO toUserProfileDTO() {
            return new UserProfileDTO()
                    .setAddress(this.address)
                    .setBirthdate(this.birthdate)
                    .setProfileImgUrl(this.profileImgUrl)
                    .setAge(this.age);
        }
    }
}
