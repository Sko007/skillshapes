package com.devoteam.skillshapes.web.rest;

import com.devoteam.skillshapes.config.Constants;
import com.devoteam.skillshapes.service.AccountService;
import com.devoteam.skillshapes.web.rest.vm.UserVM;

import io.quarkus.security.Authenticated;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST controller for managing the current user's account.
 */
@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class AccountResource {
    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private static class AccountResourceException extends RuntimeException {

        private AccountResourceException(String message) {
            super(message);
        }
    }

    // Since the resource can be accessed via an HTTP client (ie. service mode), use the Access Token as Bearer token
    @Inject
    JsonWebToken accessToken;

    /**
     * {@code GET /account} : get the current user.
     *
     * @return the current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @GET
    @Path("/account")
    @Authenticated
    public UserVM getAccount() {
        return AccountService.getAccount(accessToken);
    }

}
