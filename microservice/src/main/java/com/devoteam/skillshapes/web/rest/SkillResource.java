package com.devoteam.skillshapes.web.rest;

import static javax.ws.rs.core.UriBuilder.fromPath;

import com.devoteam.skillshapes.service.SkillService;
import com.devoteam.skillshapes.web.rest.errors.BadRequestAlertException;
import com.devoteam.skillshapes.web.util.HeaderUtil;
import com.devoteam.skillshapes.web.util.ResponseUtil;
import com.devoteam.skillshapes.service.dto.SkillDTO;

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


    @Inject
    SkillService skillService;
    /**
     * {@code POST  /skills} : Create a new skill.
     *
     * @param skillDTO the skillDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new skillDTO, or with status {@code 400 (Bad Request)} if the skill has already an ID.
     */
    @POST
    public Response createSkill(@Valid SkillDTO skillDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save Skill : {}", skillDTO);
        if (skillDTO.id != null) {
            throw new BadRequestAlertException("A new skill cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = skillService.persistOrUpdate(skillDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /skills} : Updates an existing skill.
     *
     * @param skillDTO the skillDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated skillDTO,
     * or with status {@code 400 (Bad Request)} if the skillDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the skillDTO couldn't be updated.
     */
    @PUT
    public Response updateSkill(@Valid SkillDTO skillDTO) {
        log.debug("REST request to update Skill : {}", skillDTO);
        if (skillDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = skillService.persistOrUpdate(skillDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, skillDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /skills/:id} : delete the "id" skill.
     *
     * @param id the id of the skillDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteSkill(@PathParam("id") Long id) {
        log.debug("REST request to delete Skill : {}", id);
        skillService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /skills} : get all the skills.
     *     * @return the {@link Response} with status {@code 200 (OK)} and the list of skills in body.
     */
    @GET
    public List<SkillDTO> getAllSkills() {
        log.debug("REST request to get all Skills");
        return skillService.findAll();
    }


    /**
     * {@code GET  /skills/:id} : get the "id" skill.
     *
     * @param id the id of the skillDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the skillDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")

    public Response getSkill(@PathParam("id") Long id) {
        log.debug("REST request to get Skill : {}", id);
        Optional<SkillDTO> skillDTO = skillService.findOne(id);
        return ResponseUtil.wrapOrNotFound(skillDTO);
    }
}
