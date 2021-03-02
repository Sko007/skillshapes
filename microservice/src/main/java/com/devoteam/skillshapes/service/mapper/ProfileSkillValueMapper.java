package com.devoteam.skillshapes.service.mapper;


import com.devoteam.skillshapes.domain.*;
import com.devoteam.skillshapes.service.dto.ProfileSkillValueDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProfileSkillValue} and its DTO {@link ProfileSkillValueDTO}.
 */
@Mapper(componentModel = "cdi", uses = {SkillMapper.class})
public interface ProfileSkillValueMapper extends EntityMapper<ProfileSkillValueDTO, ProfileSkillValue> {

    @Mapping(source = "skill.id", target = "skillId")
    ProfileSkillValueDTO toDto(ProfileSkillValue profileSkillValue);

    @Mapping(source = "skillId", target = "skill")
    @Mapping(target = "skillshapes", ignore = true)
    ProfileSkillValue toEntity(ProfileSkillValueDTO profileSkillValueDTO);

    default ProfileSkillValue fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProfileSkillValue profileSkillValue = new ProfileSkillValue();
        profileSkillValue.id = id;
        return profileSkillValue;
    }
}
