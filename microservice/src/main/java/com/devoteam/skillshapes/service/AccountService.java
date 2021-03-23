package com.devoteam.skillshapes.service;

import com.devoteam.skillshapes.config.Constants;
import com.devoteam.skillshapes.web.rest.AccountResource;
import com.devoteam.skillshapes.web.rest.vm.UserVM;
import org.eclipse.microprofile.jwt.JsonWebToken;

public class AccountService {

    private static class AccountServiceException extends RuntimeException {

        private AccountServiceException(String message) {
            super(message);
        }
    }

    public static UserVM getAccount(JsonWebToken accessToken){
        if (accessToken == null) {
            throw new AccountService.AccountServiceException("User could not be found");
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
