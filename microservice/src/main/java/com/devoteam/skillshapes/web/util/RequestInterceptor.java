package com.devoteam.skillshapes.web.util;

import com.devoteam.skillshapes.service.AccountService;
import com.devoteam.skillshapes.service.UserProfileService;
import com.devoteam.skillshapes.service.dto.UserProfileDTO;
import com.devoteam.skillshapes.web.rest.vm.UserVM;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.annotations.MessageLogger;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.transaction.Transactional;
import java.util.Optional;

public class RequestInterceptor {

    @Inject
    JsonWebToken accessToken;

    @Inject
    UserProfileService userProfileService;


    Object getUser(InvocationContext context) {
        Object ret = null;
        System.out.print("TEST");
        UserVM user = AccountService.getAccount(accessToken);
        Optional<UserProfileDTO> optionalUser = userProfileService.findOneByEmail(user.email);
        if(optionalUser.isPresent()) {
            UserProfileDTO userProfile = optionalUser.get();
        }
        try{
            ret = context.proceed();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return ret;
    }

}
