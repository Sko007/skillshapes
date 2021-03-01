package com.devoteam.skillshapes.web.rest;

import static javax.ws.rs.core.UriBuilder.fromPath;

import com.devoteam.skillshapes.domain.UserProfile;
import com.devoteam.skillshapes.web.rest.errors.BadRequestAlertException;
import com.devoteam.skillshapes.web.util.HeaderUtil;
import com.devoteam.skillshapes.web.util.ResponseUtil;

import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.jboss.resteasy.annotations.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;

import org.jboss.resteasy.annotations.jaxrs.QueryParam;

/**
 * REST controller for managing {@link com.devoteam.skillshapes.domain.UserProfile}.
 */
@Path("/api/user-profiles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class UserProfileResource {

    private final Logger log = LoggerFactory.getLogger(UserProfileResource.class);

    private static final String ENTITY_NAME = "skillshapesUserProfile";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    SearchSession searchSession;

    @Transactional
    void onStart(@Observes StartupEvent ev) throws InterruptedException {
        // only reindex if we imported some content
        if(UserProfile.count() > 0){
            searchSession.massIndexer().startAndWait();
        }
    }

    @GET
    @Path("search")
    @Transactional
    public List<UserProfile> searchUserProfiles(@QueryParam String pattern,
                                                @QueryParam Optional<Integer> size){

        return searchSession.search(UserProfile.class)
            .where(f ->
                pattern == null || pattern.trim().isEmpty() ?
                    f.matchAll() :
                    f.simpleQueryString().fields("firstName","lastName","email").matching(pattern)
            )
            .sort( f-> f.field("lastName_sort").then().field("firstName_sort"))
            .fetchHits(size.orElse(10));
    }



    /**
     * {@code POST  /user-profiles} : Create a new userProfile.
     *
     * @param userProfile the userProfile to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new userProfile, or with status {@code 400 (Bad Request)} if the userProfile has already an ID.
     */
    @POST
    @Transactional
    public Response createUserProfile(UserProfile userProfile, @Context UriInfo uriInfo) {
        log.debug("REST request to save UserProfile : {}", userProfile);
        if (userProfile.id != null) {
            throw new BadRequestAlertException("A new userProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = UserProfile.persistOrUpdate(userProfile);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /user-profiles} : Updates an existing userProfile.
     *
     * @param userProfile the userProfile to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated userProfile,
     * or with status {@code 400 (Bad Request)} if the userProfile is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userProfile couldn't be updated.
     */
    @PUT
    @Transactional
    public Response updateUserProfile(UserProfile userProfile) {
        log.debug("REST request to update UserProfile : {}", userProfile);
        if (userProfile.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = UserProfile.persistOrUpdate(userProfile);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userProfile.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /user-profiles/:id} : delete the "id" userProfile.
     *
     * @param id the id of the userProfile to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteUserProfile(@PathParam("id") Long id) {
        log.debug("REST request to delete UserProfile : {}", id);
        UserProfile.findByIdOptional(id).ifPresent(userProfile -> {
            userProfile.delete();
        });
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /user-profiles} : get all the userProfiles.
     *     * @return the {@link Response} with status {@code 200 (OK)} and the list of userProfiles in body.
     */
    @GET
    public List<UserProfile> getAllUserProfiles() {
        log.debug("REST request to get all UserProfiles");
        return UserProfile.findAll().list();
    }


    /**
     * {@code GET  /user-profiles/:id} : get the "id" userProfile.
     *
     * @param id the id of the userProfile to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the userProfile, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")

    public Response getUserProfile(@PathParam("id") Long id) {
        log.debug("REST request to get UserProfile : {}", id);
        Optional<UserProfile> userProfile = UserProfile.findByIdOptional(id);
        return ResponseUtil.wrapOrNotFound(userProfile);
    }
}
