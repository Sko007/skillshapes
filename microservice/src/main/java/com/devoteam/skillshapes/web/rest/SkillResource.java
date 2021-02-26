package com.devoteam.skillshapes.web.rest;

import static javax.ws.rs.core.UriBuilder.fromPath;

import com.devoteam.skillshapes.domain.Skill;
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
 * REST controller for managing {@link com.devoteam.skillshapes.domain.Skill}.
 */
@Path("/api/skills")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class SkillResource {

    private final Logger log = LoggerFactory.getLogger(SkillResource.class);

    private static final String ENTITY_NAME = "skillshapesSkill";

    @ConfigProperty(name = "application.name")
    String applicationName;


    
    /**
     * {@code POST  /skills} : Create a new skill.
     *
     * @param skill the skill to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new skill, or with status {@code 400 (Bad Request)} if the skill has already an ID.
     */
    @POST
    @Transactional
    public Response createSkill(@Valid Skill skill, @Context UriInfo uriInfo) {
        log.debug("REST request to save Skill : {}", skill);
        if (skill.id != null) {
            throw new BadRequestAlertException("A new skill cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = Skill.persistOrUpdate(skill);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /skills} : Updates an existing skill.
     *
     * @param skill the skill to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated skill,
     * or with status {@code 400 (Bad Request)} if the skill is not valid,
     * or with status {@code 500 (Internal Server Error)} if the skill couldn't be updated.
     */
    @PUT
    @Transactional
    public Response updateSkill(@Valid Skill skill) {
        log.debug("REST request to update Skill : {}", skill);
        if (skill.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = Skill.persistOrUpdate(skill);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, skill.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /skills/:id} : delete the "id" skill.
     *
     * @param id the id of the skill to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteSkill(@PathParam("id") Long id) {
        log.debug("REST request to delete Skill : {}", id);
        Skill.findByIdOptional(id).ifPresent(skill -> {
            skill.delete();
        });
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /skills} : get all the skills.
     *     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link Response} with status {@code 200 (OK)} and the list of skills in body.
     */
    @GET
    @Transactional
    public List<Skill> getAllSkills(@QueryParam(value = "eagerload") boolean eagerload) {
        log.debug("REST request to get all Skills");
        return Skill.findAllWithEagerRelationships().list();
    }


    /**
     * {@code GET  /skills/:id} : get the "id" skill.
     *
     * @param id the id of the skill to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the skill, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")

    public Response getSkill(@PathParam("id") Long id) {
        log.debug("REST request to get Skill : {}", id);
        Optional<Skill> skill = Skill.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(skill);
    }
}
