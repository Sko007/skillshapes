package com.devoteam.skillshapes.web.rest;

import static javax.ws.rs.core.UriBuilder.fromPath;

import com.devoteam.skillshapes.service.ProfileSkillValueService;
import com.devoteam.skillshapes.web.rest.errors.BadRequestAlertException;
import com.devoteam.skillshapes.web.util.HeaderUtil;
import com.devoteam.skillshapes.web.util.ResponseUtil;
import com.devoteam.skillshapes.service.dto.ProfileSkillValueDTO;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.devoteam.skillshapes.domain.ProfileSkillValue}.
 */
@Path("/api/profile-skill-values")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class ProfileSkillValueResource {

    private final Logger log = LoggerFactory.getLogger(ProfileSkillValueResource.class);

    private static final String ENTITY_NAME = "skillshapesProfileSkillValue";

    @ConfigProperty(name = "application.name")
    String applicationName;


    @Inject
    ProfileSkillValueService profileSkillValueService;
    /**
     * {@code POST  /profile-skill-values} : Create a new profileSkillValue.
     *
     * @param profileSkillValueDTO the profileSkillValueDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new profileSkillValueDTO, or with status {@code 400 (Bad Request)} if the profileSkillValue has already an ID.
     */
    @POST
    public Response createProfileSkillValue(@Valid ProfileSkillValueDTO profileSkillValueDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save ProfileSkillValue : {}", profileSkillValueDTO);
        if (profileSkillValueDTO.id != null) {
            throw new BadRequestAlertException("A new profileSkillValue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = profileSkillValueService.persistOrUpdate(profileSkillValueDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /profile-skill-values} : Updates an existing profileSkillValue.
     *
     * @param profileSkillValueDTO the profileSkillValueDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated profileSkillValueDTO,
     * or with status {@code 400 (Bad Request)} if the profileSkillValueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the profileSkillValueDTO couldn't be updated.
     */
    @PUT
    public Response updateProfileSkillValue(@Valid ProfileSkillValueDTO profileSkillValueDTO) {
        log.debug("REST request to update ProfileSkillValue : {}", profileSkillValueDTO);
        if (profileSkillValueDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = profileSkillValueService.persistOrUpdate(profileSkillValueDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profileSkillValueDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /profile-skill-values/:id} : delete the "id" profileSkillValue.
     *
     * @param id the id of the profileSkillValueDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteProfileSkillValue(@PathParam("id") Long id) {
        log.debug("REST request to delete ProfileSkillValue : {}", id);
        profileSkillValueService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /profile-skill-values} : get all the profileSkillValues.
     *     * @return the {@link Response} with status {@code 200 (OK)} and the list of profileSkillValues in body.
     */
    @GET
    public List<ProfileSkillValueDTO> getAllProfileSkillValues() {
        log.debug("REST request to get all ProfileSkillValues");
        return profileSkillValueService.findAll();
    }


    /**
     * {@code GET  /profile-skill-values/:id} : get the "id" profileSkillValue.
     *
     * @param id the id of the profileSkillValueDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the profileSkillValueDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")

    public Response getProfileSkillValue(@PathParam("id") Long id) {
        log.debug("REST request to get ProfileSkillValue : {}", id);
        Optional<ProfileSkillValueDTO> profileSkillValueDTO = profileSkillValueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(profileSkillValueDTO);
    }
}
