package com.example.demo.domain.userprofile.dto;

import com.example.demo.core.generic.AbstractDTO;
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

    private String address;
    private LocalDate birthdate;
    private String profileImgUrl;
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
}