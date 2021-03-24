package com.devoteam.skillshapes.web.rest;

import static javax.ws.rs.core.UriBuilder.fromPath;

import com.devoteam.skillshapes.annotations.SearchableEntity;
import com.devoteam.skillshapes.service.AccountService;
import com.devoteam.skillshapes.service.SkillShapeService;
import com.devoteam.skillshapes.service.UserProfileService;
import com.devoteam.skillshapes.service.dto.UserProfileDTO;
import com.devoteam.skillshapes.web.rest.errors.BadRequestAlertException;
import com.devoteam.skillshapes.web.rest.vm.UserVM;
import com.devoteam.skillshapes.web.util.HeaderUtil;
import com.devoteam.skillshapes.web.util.ResponseUtil;
import com.devoteam.skillshapes.service.dto.SkillShapeDTO;

import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;
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
 * REST controller for managing {@link com.devoteam.skillshapes.domain.SkillShape}.
 */
@SearchableEntity("com.devoteam.skillshapes.domain.SkillShape")
@Path("/api/skill-shapes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Authenticated
public class SkillShapeResource extends SearchResource{

    private final Logger log = LoggerFactory.getLogger(SkillShapeResource.class);

    private static final String ENTITY_NAME = "skillshapesSkillShape";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    SkillShapeService skillShapeService;

    @Inject
    UserProfileService userProfileService;



    /**
     * {@code POST  /skill-shapes} : Create a new skillShape.
     *
     * @param skillShapeDTO the skillShapeDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new skillShapeDTO, or with status {@code 400 (Bad Request)} if the skillShape has already an ID.
     */
    @POST
    public Response createSkillShape(@Valid SkillShapeDTO skillShapeDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save SkillShape : {}", skillShapeDTO);
        if (skillShapeDTO.id != null) {
            throw new BadRequestAlertException("A new skillShape cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = skillShapeService.persistOrUpdate(skillShapeDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /skill-shapes} : Updates an existing skillShape.
     *
     * @param skillShapeDTO the skillShapeDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated skillShapeDTO,
     * or with status {@code 400 (Bad Request)} if the skillShapeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the skillShapeDTO couldn't be updated.
     */
    @PUT
    public Response updateSkillShape(@Valid SkillShapeDTO skillShapeDTO) {
        log.debug("REST request to update SkillShape : {}", skillShapeDTO);
        if (skillShapeDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = skillShapeService.persistOrUpdate(skillShapeDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, skillShapeDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /skill-shapes/:id} : delete the "id" skillShape.
     *
     * @param id the id of the skillShapeDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteSkillShape(@PathParam("id") Long id) {
        log.debug("REST request to delete SkillShape : {}", id);
        skillShapeService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /skill-shapes} : get all the skillShapes.
     *     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link Response} with status {@code 200 (OK)} and the list of skillShapes in body.
     */
    @GET
    public List<SkillShapeDTO> getAllSkillShapes(@QueryParam(value = "eagerload") boolean eagerload) {
        log.debug("REST request to get all SkillShapes");
        UserProfileDTO userProfile = userProfileService.userProfileDTO;
        if(userProfile != null && userProfile.isAdmin()) return skillShapeService.findAll();
        return skillShapeService.findAllByUserID(userProfile.id);
    }


    /**
     * {@code GET  /skill-shapes/:id} : get the "id" skillShape.
     *
     * @param id the id of the skillShapeDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the skillShapeDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getSkillShape(@PathParam("id") Long id) {
        log.debug("REST request to get SkillShape : {}", id);
        Optional<SkillShapeDTO> skillShapeDTO = skillShapeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(skillShapeDTO);
    }
}
