package com.example.demo.domain.userprofile.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class UserProfileCreateUpdateDTO {

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "Birthdate is required")
    private LocalDate birthdate;

    @Size(max = 1024)
    private String profileImgUrl; // optional

    @NotNull(message = "Age is required")
    @Min(value = 0, message = "Age must be >= 0")
    @Max(value = 150, message = "Age must be <= 150")
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
}
