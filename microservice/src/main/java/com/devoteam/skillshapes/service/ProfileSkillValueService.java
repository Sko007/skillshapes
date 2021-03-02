package com.devoteam.skillshapes.service;

import com.devoteam.skillshapes.domain.ProfileSkillValue;
import com.devoteam.skillshapes.service.dto.ProfileSkillValueDTO;
import com.devoteam.skillshapes.service.mapper.ProfileSkillValueMapper;
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
public class ProfileSkillValueService {

    private final Logger log = LoggerFactory.getLogger(ProfileSkillValueService.class);

    @Inject
    ProfileSkillValueMapper profileSkillValueMapper;

    @Transactional
    public ProfileSkillValueDTO persistOrUpdate(ProfileSkillValueDTO profileSkillValueDTO) {
        log.debug("Request to save ProfileSkillValue : {}", profileSkillValueDTO);
        var profileSkillValue = profileSkillValueMapper.toEntity(profileSkillValueDTO);
        profileSkillValue = ProfileSkillValue.persistOrUpdate(profileSkillValue);
        return profileSkillValueMapper.toDto(profileSkillValue);
    }

    /**
     * Delete the ProfileSkillValue by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete ProfileSkillValue : {}", id);
        ProfileSkillValue.findByIdOptional(id).ifPresent(profileSkillValue -> {
            profileSkillValue.delete();
        });
    }

    /**
     * Get one profileSkillValue by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<ProfileSkillValueDTO> findOne(Long id) {
        log.debug("Request to get ProfileSkillValue : {}", id);
        return ProfileSkillValue.findByIdOptional(id)
            .map(profileSkillValue -> profileSkillValueMapper.toDto((ProfileSkillValue) profileSkillValue)); 
    }

    /**
     * Get all the profileSkillValues.
     * @return the list of entities.
     */
    public  List<ProfileSkillValueDTO> findAll() {
        log.debug("Request to get all ProfileSkillValues");
        List<ProfileSkillValue> profileSkillValues = ProfileSkillValue.findAll().list();
        return profileSkillValueMapper.toDto(profileSkillValues);
    }



}
