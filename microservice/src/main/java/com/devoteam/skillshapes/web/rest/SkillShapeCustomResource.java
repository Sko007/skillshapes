package com.devoteam.skillshapes.web.rest;

import com.devoteam.skillshapes.service.AccountService;
import com.devoteam.skillshapes.service.SkillShapeCustomService;
import com.devoteam.skillshapes.service.UserProfileService;
import com.devoteam.skillshapes.service.dto.SkillShapeCustomDTO;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/api/skill-shapes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Authenticated
public class SkillShapeCustomResource {

    private final Logger log = LoggerFactory.getLogger(SkillShapeResource.class);

    @Inject
    SkillShapeCustomService skillShapeService;

    @Inject
    UserProfileService userProfileService;

    @Inject
    AccountService accountService;

    @Inject
    JsonWebToken accessToken;

    /**
     * {@code GET  /skill-shapes/profile/:id} : get all skillShapes of the "ownerId".
     *
     * @param ownerId the owner id of the skillShapeDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the skillShapeDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("profile/{id}")
    public List<SkillShapeCustomDTO> getSkillShapeByProfileId(@PathParam("id") Long ownerId) {
        log.debug("REST request to get SkillShape of Profile : {}", ownerId);
        return skillShapeService.findAllWithEagerRelationshipsByUserProfileId(ownerId);
    }

    /**
     * {@code GET  /skill-shapes/profile} : get all skillShapes of the logged in user.
     *
     * @return the {@link Response} with status {@code 200 (OK)} and with body the skillShapeDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("profile")
    public List<SkillShapeCustomDTO> getSkillShapeByProfile() {
        return accountService.getAccountUserProfile()
            .map(user -> skillShapeService.findAllWithEagerRelationshipsByUserProfileId(user.id))
            .orElseThrow(() -> new BadRequestException("User not found"));
    }
}
