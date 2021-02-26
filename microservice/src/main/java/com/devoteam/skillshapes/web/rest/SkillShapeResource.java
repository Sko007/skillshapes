package com.devoteam.skillshapes.web.rest;

import static javax.ws.rs.core.UriBuilder.fromPath;

import com.devoteam.skillshapes.domain.SkillShape;
import com.devoteam.skillshapes.web.rest.errors.BadRequestAlertException;
import com.devoteam.skillshapes.web.util.HeaderUtil;
import com.devoteam.skillshapes.web.util.ResponseUtil;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.devoteam.skillshapes.domain.SkillShape}.
 */
@Path("/api/skill-shapes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class SkillShapeResource {

    private final Logger log = LoggerFactory.getLogger(SkillShapeResource.class);

    private static final String ENTITY_NAME = "skillshapesSkillShape";

    @ConfigProperty(name = "application.name")
    String applicationName;


    
    /**
     * {@code POST  /skill-shapes} : Create a new skillShape.
     *
     * @param skillShape the skillShape to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new skillShape, or with status {@code 400 (Bad Request)} if the skillShape has already an ID.
     */
    @POST
    @Transactional
    public Response createSkillShape(@Valid SkillShape skillShape, @Context UriInfo uriInfo) {
        log.debug("REST request to save SkillShape : {}", skillShape);
        if (skillShape.id != null) {
            throw new BadRequestAlertException("A new skillShape cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = SkillShape.persistOrUpdate(skillShape);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /skill-shapes} : Updates an existing skillShape.
     *
     * @param skillShape the skillShape to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated skillShape,
     * or with status {@code 400 (Bad Request)} if the skillShape is not valid,
     * or with status {@code 500 (Internal Server Error)} if the skillShape couldn't be updated.
     */
    @PUT
    @Transactional
    public Response updateSkillShape(@Valid SkillShape skillShape) {
        log.debug("REST request to update SkillShape : {}", skillShape);
        if (skillShape.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = SkillShape.persistOrUpdate(skillShape);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, skillShape.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /skill-shapes/:id} : delete the "id" skillShape.
     *
     * @param id the id of the skillShape to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteSkillShape(@PathParam("id") Long id) {
        log.debug("REST request to delete SkillShape : {}", id);
        SkillShape.findByIdOptional(id).ifPresent(skillShape -> {
            skillShape.delete();
        });
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
    @Transactional
    public List<SkillShape> getAllSkillShapes(@QueryParam(value = "eagerload") boolean eagerload) {
        log.debug("REST request to get all SkillShapes");
        return SkillShape.findAllWithEagerRelationships().list();
    }


    /**
     * {@code GET  /skill-shapes/:id} : get the "id" skillShape.
     *
     * @param id the id of the skillShape to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the skillShape, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")

    public Response getSkillShape(@PathParam("id") Long id) {
        log.debug("REST request to get SkillShape : {}", id);
        Optional<SkillShape> skillShape = SkillShape.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(skillShape);
    }
}
