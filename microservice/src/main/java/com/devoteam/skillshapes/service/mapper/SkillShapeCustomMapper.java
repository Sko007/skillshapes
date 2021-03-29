package com.devoteam.skillshapes.service.mapper;


import com.devoteam.skillshapes.domain.SkillShape;
import com.devoteam.skillshapes.service.dto.SkillShapeCustomDTO;
import com.devoteam.skillshapes.service.dto.SkillShapeDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link SkillShape} and its DTO {@link SkillShapeDTO}.
 */
@Mapper(componentModel = "cdi", uses = {ProfileSkillValueMapper.class, UserProfileMapper.class})
public interface SkillShapeCustomMapper extends EntityMapper<SkillShapeCustomDTO, SkillShape> {

    default SkillShape fromId(Long id) {
        if (id == null) {
            return null;
        }
        SkillShape skillShape = new SkillShape();
        skillShape.id = id;
        return skillShape;
    }
}
