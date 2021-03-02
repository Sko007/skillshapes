package com.devoteam.skillshapes.service;

import com.devoteam.skillshapes.domain.Skill;
import com.devoteam.skillshapes.service.dto.SkillDTO;
import com.devoteam.skillshapes.service.mapper.SkillMapper;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class SkillService {

    private final Logger log = LoggerFactory.getLogger(SkillService.class);

    @Inject
    SkillMapper skillMapper;

    @Transactional
    public SkillDTO persistOrUpdate(SkillDTO skillDTO) {
        log.debug("Request to save Skill : {}", skillDTO);
        var skill = skillMapper.toEntity(skillDTO);
        skill = Skill.persistOrUpdate(skill);
        return skillMapper.toDto(skill);
    }

    /**
     * Delete the Skill by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Skill : {}", id);
        Skill.findByIdOptional(id).ifPresent(skill -> {
            skill.delete();
        });
    }

    /**
     * Get one skill by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<SkillDTO> findOne(Long id) {
        log.debug("Request to get Skill : {}", id);
        return Skill.findByIdOptional(id)
            .map(skill -> skillMapper.toDto((Skill) skill)); 
    }

    /**
     * Get all the skills.
     * @return the list of entities.
     */
    public  List<SkillDTO> findAll() {
        log.debug("Request to get all Skills");
        List<Skill> skills = Skill.findAll().list();
        return skillMapper.toDto(skills);
    }



}
