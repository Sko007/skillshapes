package com.devoteam.skillshapes.service;

import com.devoteam.skillshapes.domain.Skill;
import com.devoteam.skillshapes.domain.SkillShape;
import com.devoteam.skillshapes.service.dto.SkillShapeDTO;
import com.devoteam.skillshapes.service.mapper.SkillShapeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class SkillShapeService {

    private final Logger log = LoggerFactory.getLogger(SkillShapeService.class);

    @Inject
    SkillShapeMapper skillShapeMapper;

    @Transactional
    public SkillShapeDTO persistOrUpdate(SkillShapeDTO skillShapeDTO) {
        log.debug("Request to save SkillShape : {}", skillShapeDTO);
        var skillShape = skillShapeMapper.toEntity(skillShapeDTO);
        skillShape = SkillShape.persistOrUpdate(skillShape);
        return skillShapeMapper.toDto(skillShape);
    }

    /**
     * Delete the SkillShape by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete SkillShape : {}", id);
        SkillShape.findByIdOptional(id).ifPresent(skillShape -> {
            skillShape.delete();
        });
    }

    /**
     * Get one skillShape by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<SkillShapeDTO> findOne(Long id) {
        log.debug("Request to get SkillShape : {}", id);
        return SkillShape.findOneWithEagerRelationships(id)
            .map(skillShape -> skillShapeMapper.toDto((SkillShape) skillShape));
    }

    /**
     * Get all the skillShapes.
     * @return the list of entities.
     */
    public  List<SkillShapeDTO> findAll() {
        log.debug("Request to get all SkillShapes");
        List<SkillShape> skillShapes = SkillShape.findAllWithEagerRelationships().list();
        return skillShapeMapper.toDto(skillShapes);
    }

    /**
     * Get all the skillShapes with eager load of many-to-many relationships.
     * @return the list of entities.
     */
    public  List<SkillShapeDTO> findAllWithEagerRelationships() {
        List<SkillShape> skillShapes = SkillShape.findAllWithEagerRelationships().list();
        return skillShapeMapper.toDto(skillShapes);
    }

    /**
     * Get aöö tje skillShapes that belong to one User.
     * @param id User id
     * @return the list of entities
     */
    public List<SkillShapeDTO> findAllByUserID(Long id){
        List<SkillShape> skillShapes = SkillShape.findAllByUserId(id).list();
        return skillShapeMapper.toDto(skillShapes);
    }
}
