package com.devoteam.skillshapes.service.mapper;


import com.devoteam.skillshapes.domain.*;
import com.devoteam.skillshapes.service.dto.SkillShapeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SkillShape} and its DTO {@link SkillShapeDTO}.
 */
@Mapper(componentModel = "cdi", uses = {ProfileSkillValueMapper.class, UserProfileMapper.class})
public interface SkillShapeMapper extends EntityMapper<SkillShapeDTO, SkillShape> {



    default SkillShape fromId(Long id) {
        if (id == null) {
            return null;
        }
        SkillShape skillShape = new SkillShape();
        skillShape.id = id;
        return skillShape;
    }
}
