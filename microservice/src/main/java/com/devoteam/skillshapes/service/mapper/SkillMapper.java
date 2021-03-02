package com.devoteam.skillshapes.service.mapper;


import com.devoteam.skillshapes.domain.*;
import com.devoteam.skillshapes.service.dto.SkillDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Skill} and its DTO {@link SkillDTO}.
 */
@Mapper(componentModel = "cdi", uses = {})
public interface SkillMapper extends EntityMapper<SkillDTO, Skill> {



    default Skill fromId(Long id) {
        if (id == null) {
            return null;
        }
        Skill skill = new Skill();
        skill.id = id;
        return skill;
    }
}
