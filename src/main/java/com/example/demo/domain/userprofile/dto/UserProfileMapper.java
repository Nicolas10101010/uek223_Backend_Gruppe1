package com.example.demo.domain.userprofile.dto;

import com.example.demo.core.generic.AbstractMapper;
import com.example.demo.domain.userprofile.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserProfileMapper extends AbstractMapper<UserProfile, UserProfileDTO> {

    @Override
    UserProfileDTO toDTO(UserProfile entity);

    UserProfile toEntity(UserProfileDTO.CreateUpdateDTO dto);

    void updateEntityFromDTO(UserProfileDTO.CreateUpdateDTO dto, @MappingTarget UserProfile entity);
}
