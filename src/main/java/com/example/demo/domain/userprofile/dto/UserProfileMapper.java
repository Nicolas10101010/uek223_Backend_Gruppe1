package com.example.demo.domain.userprofile.dto;

import com.example.demo.domain.userprofile.UserProfile;
import com.example.demo.domain.userprofile.dto.UserProfileDTO;
import org.springframework.stereotype.Component;

@Component
public class UserProfileMapper {

    public UserProfileDTO toDTO(UserProfile entity) {
        if (entity == null) {
            return null;
        }

        return new UserProfileDTO(
                entity.getId(),
                entity.getAddress(),
                entity.getBirthdate(),
                entity.getProfileImgUrl(),
                entity.getAge(),
                entity.getUser() != null ? entity.getUser().getId() : null,
                entity.getUser() != null ? entity.getUser().getFirstName() : null,
                entity.getUser() != null ? entity.getUser().getLastName() : null,
                entity.getUser() != null ? entity.getUser().getEmail() : null
        );
    }

    public UserProfile toEntity(UserProfileDTO dto) {
        if (dto == null) {
            return null;
        }

        UserProfile entity = new UserProfile();
        entity.setAddress(dto.address());
        entity.setBirthdate(dto.birthdate());
        entity.setProfileImgUrl(dto.profileImgUrl());
        entity.setAge(dto.age());

        return entity;
    }

    public void updateEntityFromDTO(UserProfileDTO dto, UserProfile entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.address() != null) {
            entity.setAddress(dto.address());
        }
        if (dto.birthdate() != null) {
            entity.setBirthdate(dto.birthdate());
        }
        if (dto.profileImgUrl() != null) {
            entity.setProfileImgUrl(dto.profileImgUrl());
        }
        if (dto.age() != null) {
            entity.setAge(dto.age());
        }
    }
}