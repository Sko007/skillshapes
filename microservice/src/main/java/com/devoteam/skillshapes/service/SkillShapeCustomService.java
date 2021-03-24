package com.devoteam.skillshapes.service;

import com.devoteam.skillshapes.domain.CustomSkillShape;
import com.devoteam.skillshapes.domain.SkillShape;
import com.devoteam.skillshapes.service.dto.SkillShapeCustomDTO;
import com.devoteam.skillshapes.service.dto.SkillShapeDTO;
import com.devoteam.skillshapes.service.mapper.SkillShapeCustomMapper;
import com.devoteam.skillshapes.service.mapper.SkillShapeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class SkillShapeCustomService {

    private final Logger log = LoggerFactory.getLogger(SkillShapeService.class);

    @Inject
    SkillShapeCustomMapper skillShapeCustomMapper;

    /**
     * Get all the skillShapes with eager load of many-to-many relationships.
     * @return the list of entities.
     */
    public List<SkillShapeCustomDTO> findAllWithEagerRelationshipsByUserProfileId(Long ownerId) {
        List<SkillShape> skillShapes = CustomSkillShape.findAllWithEagerRelationshipsByUserProfileId(ownerId).list();
        return skillShapeCustomMapper.toDto(skillShapes);
    }
}
