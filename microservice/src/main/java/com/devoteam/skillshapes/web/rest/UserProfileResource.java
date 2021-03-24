package com.devoteam.skillshapes.web.rest;

import com.devoteam.skillshapes.annotations.SearchableEntity;
import com.devoteam.skillshapes.service.AccountService;
import com.devoteam.skillshapes.service.UserProfileService;
import com.devoteam.skillshapes.service.dto.UserProfileDTO;
import com.devoteam.skillshapes.web.rest.errors.BadRequestAlertException;
import com.devoteam.skillshapes.web.util.HeaderUtil;
import com.devoteam.skillshapes.web.util.ResponseUtil;
import io.quarkus.security.Authenticated;
import io.quarkus.security.ForbiddenException;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Collections;
import java.util.List;

import static javax.ws.rs.core.UriBuilder.fromPath;

/**
 * REST controller for managing {@link com.devoteam.skillshapes.domain.UserProfile}.
 */
@SearchableEntity("com.devoteam.skillshapes.domain.UserProfile")
@Path("/api/user-profiles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Authenticated
public class UserProfileResource extends SearchResource {

    private static final String ENTITY_NAME = "skillshapesUserProfile";
    private final Logger log = LoggerFactory.getLogger(UserProfileResource.class);
    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    UserProfileService userProfileService;

    @Inject
    AccountService accountService;

    /**
     * {@code POST  /user-profiles} : Create a new userProfile.
     *
     * @param userProfileDTO the userProfileDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new userProfileDTO, or with status {@code 400 (Bad Request)} if the userProfile has already an ID.
     */
    @POST
    public Response createUserProfile(UserProfileDTO userProfileDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save UserProfile : {}", userProfileDTO);
        if (userProfileDTO.id != null) {
            throw new BadRequestAlertException("A new userProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = userProfileService.persistOrUpdate(userProfileDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /user-profiles} : Updates an existing userProfile.
     *
     * @param userProfileDTO the userProfileDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated userProfileDTO,
     * or with status {@code 400 (Bad Request)} if the userProfileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userProfileDTO couldn't be updated.
     */
    @PUT
    public Response updateUserProfile(UserProfileDTO userProfileDTO) {
        log.debug("REST request to update UserProfile : {}", userProfileDTO);
        if (userProfileDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = userProfileService.persistOrUpdate(userProfileDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userProfileDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /user-profiles/:id} : delete the "id" userProfile.
     *
     * @param id the id of the userProfileDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteUserProfile(@PathParam("id") Long id) {
        log.debug("REST request to delete UserProfile : {}", id);
        userProfileService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /user-profiles} : get all the userProfiles.
     * * @return the {@link Response} with status {@code 200 (OK)} and the list of userProfiles in body.
     */
    @GET
    public List<UserProfileDTO> getAllUserProfiles() {
        log.info("REST request to get all UserProfiles");

        return accountService.getAccountUserProfile()
            .map(user -> user.isAdmin() ? userProfileService.findAll() : Collections.singletonList(user))
            .orElseThrow(() -> new ForbiddenException("Not authorized"));
    }

    /**
     * {@code GET  /user-profiles/:id} : get the "id" userProfile.
     *
     * @param id the id of the userProfileDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the userProfileDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getUserProfile(@PathParam("id") Long id) {
        log.debug("REST request to get UserProfile : {}", id);

        return ResponseUtil.wrapOrNotFound(accountService.getAccountUserProfile()
            .filter(user -> user.isAdmin() || Long.compare(user.id, id) == 0)
            .map(ignore -> userProfileService.findOne(id))
            .orElseThrow(() -> new ForbiddenException("Not authorized")));
    }
}
