package com.devoteam.skillshapes.web.rest;

import com.devoteam.skillshapes.config.Constants;
import com.devoteam.skillshapes.web.rest.vm.UserVM;
import io.quarkus.oidc.IdToken;
import io.quarkus.oidc.RefreshToken;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class UserResource {
    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private static class AccountResourceException extends RuntimeException {

        private AccountResourceException(String message) {
            super(message);
        }
    }

    /**
     * Injection point for the Access Token issued by the OpenID Connect Provider
     */
    @Inject
    JsonWebToken accessToken;


    @GET
    @Path("/user")
    @Authenticated
    public UserVM getAccount() {
        if (accessToken == null) {
            throw new UserResource.AccountResourceException("User could not be found");
        }

        UserVM user = new UserVM();
        // handle resource server JWT, where sub claim is email and uid is ID
        if (accessToken.getClaim("uid") != null) {
            user.id = accessToken.getClaim("uid");
            user.login = accessToken.getClaim("sub");
        } else {
            user.id = accessToken.getClaim("sub");
        }
        if (accessToken.getClaim("preferred_username") != null) {
            user.login = ((String) accessToken.getClaim("preferred_username")).toLowerCase();
        } else if (user.login == null) {
            user.login = user.id;
        }
        if (accessToken.getClaim("given_name") != null) {
            user.firstName = accessToken.getClaim("given_name");
        }
        if (accessToken.getClaim("family_name") != null) {
            user.lastName = accessToken.getClaim("family_name");
        }
        if (accessToken.getClaim("email_verified") != null) {
            user.activated = accessToken.getClaim("email_verified");
        }
        if (accessToken.getClaim("email") != null) {
            user.email = ((String) accessToken.getClaim("email")).toLowerCase();
        } else {
            user.email = accessToken.getClaim("sub");
        }
        if (accessToken.getClaim("langKey") != null) {
            user.langKey = (String) accessToken.getClaim("langKey");
        } else if (accessToken.getClaim("locale") != null) {
            // trim off country code if it exists
            String locale = accessToken.getClaim("locale");
            if (locale.contains("_")) {
                locale = locale.substring(0, locale.indexOf('_'));
            } else if (locale.contains("-")) {
                locale = locale.substring(0, locale.indexOf('-'));
            }
            user.langKey = locale.toLowerCase();
        } else {
            // set langKey to default if not specified by IdP
            user.langKey = Constants.DEFAULT_LANGUAGE;
        }
        if (accessToken.getClaim("picture") != null) {
            user.imageUrl = accessToken.getClaim("picture");
        }
        user.activated = true;
        user.authorities = accessToken.getClaim("groups");
        return user;
    }
}
