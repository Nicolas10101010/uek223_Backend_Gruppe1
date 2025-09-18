package com.example.demo.domain.userprofile.dto;

import com.example.demo.core.generic.AbstractDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
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
public class UserProfileCreateUpdateDTO extends AbstractDTO {

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "Birthdate is required")
    @Past(message = "Birthdate must be in the past")
    private LocalDate birthdate;

    private String profileImgUrl;

    @Positive(message = "Age must be positive")
    private Integer age;

    public UserProfileCreateUpdateDTO(String address,
                                      LocalDate birthdate,
                                      String profileImgUrl,
                                      Integer age) {
        this.address = address;
        this.birthdate = birthdate;
        this.profileImgUrl = profileImgUrl;
        this.age = age;
    }

    public UserProfileCreateUpdateDTO(UUID id,
                                      String address,
                                      LocalDate birthdate,
                                      String profileImgUrl,
                                      Integer age) {
        super(id);
        this.address = address;
        this.birthdate = birthdate;
        this.profileImgUrl = profileImgUrl;
        this.age = age;
    }
}