package com.devoteam.skillshapes.service.mapper;


import com.devoteam.skillshapes.domain.*;
import com.devoteam.skillshapes.service.dto.ProfileSkillValueDTO;
import com.devoteam.skillshapes.service.dto.UserProfileDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserProfile} and its DTO {@link UserProfileDTO}.
 */
@Mapper(componentModel = "cdi", uses = {})
public interface UserProfileMapper extends EntityMapper<UserProfileDTO, UserProfile> {

    @Mapping(target = "skillshapes", ignore = true)
    UserProfile toEntity(UserProfileDTO userProfileDTO);

    default UserProfile fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserProfile userProfile = new UserProfile();
        userProfile.id = id;
        return userProfile;
    }
}
