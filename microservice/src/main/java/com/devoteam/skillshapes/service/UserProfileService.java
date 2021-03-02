package com.devoteam.skillshapes.service;

import com.devoteam.skillshapes.domain.UserProfile;
import com.devoteam.skillshapes.service.dto.UserProfileDTO;
import com.devoteam.skillshapes.service.mapper.UserProfileMapper;
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
public class UserProfileService {

    private final Logger log = LoggerFactory.getLogger(UserProfileService.class);

    @Inject
    UserProfileMapper userProfileMapper;

    @Transactional
    public UserProfileDTO persistOrUpdate(UserProfileDTO userProfileDTO) {
        log.debug("Request to save UserProfile : {}", userProfileDTO);
        var userProfile = userProfileMapper.toEntity(userProfileDTO);
        userProfile = UserProfile.persistOrUpdate(userProfile);
        return userProfileMapper.toDto(userProfile);
    }

    /**
     * Delete the UserProfile by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete UserProfile : {}", id);
        UserProfile.findByIdOptional(id).ifPresent(userProfile -> {
            userProfile.delete();
        });
    }

    /**
     * Get one userProfile by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<UserProfileDTO> findOne(Long id) {
        log.debug("Request to get UserProfile : {}", id);
        return UserProfile.findByIdOptional(id)
            .map(userProfile -> userProfileMapper.toDto((UserProfile) userProfile)); 
    }

    /**
     * Get all the userProfiles.
     * @return the list of entities.
     */
    public  List<UserProfileDTO> findAll() {
        log.debug("Request to get all UserProfiles");
        List<UserProfile> userProfiles = UserProfile.findAll().list();
        return userProfileMapper.toDto(userProfiles);
    }



}
