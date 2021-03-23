package com.devoteam.skillshapes.web.util;

import com.devoteam.skillshapes.service.AccountService;
import com.devoteam.skillshapes.service.UserProfileService;
import com.devoteam.skillshapes.service.dto.UserProfileDTO;
import com.devoteam.skillshapes.web.rest.SkillShapeResource;
import com.devoteam.skillshapes.web.rest.errors.BadRequestAlertException;
import com.devoteam.skillshapes.web.rest.vm.UserVM;
import io.vertx.core.http.HttpServerRequest;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Optional;

@Provider
public class RequestFilter implements ContainerRequestFilter {
    private final Logger log = LoggerFactory.getLogger(SkillShapeResource.class);

    @Inject
    JsonWebToken accessToken;

    @Inject
    UserProfileService userProfileService;

    @Context
    UriInfo info;

    @Context
    HttpServerRequest request;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        UserVM user = AccountService.getAccount(accessToken);
        Optional<UserProfileDTO> optionalUser = userProfileService.findOneByEmail(user.email);
        if(optionalUser.isPresent()) {
            UserProfileDTO userProfile = optionalUser.get();
            userProfileService.userProfileDTO = userProfile;
        } else {
            userProfileService.userProfileDTO = null;
            throw new BadRequestAlertException("Invalid user email","User",user.email);
        }
    }
}
