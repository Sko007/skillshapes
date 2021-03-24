package com.devoteam.skillshapes.service.dto;


import com.devoteam.skillshapes.config.Constants;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;

/**
 * A DTO for the {@link com.devoteam.skillshapes.domain.UserProfile} entity.
 */
@ApiModel(description = "Devoteam Profile\n")
@RegisterForReflection
public class UserProfileDTO implements Serializable {

    public Long id;

    public String firstName;

    public String lastName;

    public String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserProfileDTO)) {
            return false;
        }

        return id != null && id.equals(((UserProfileDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "UserProfileDTO{" +
            "id=" + id +
            ", firstName='" + firstName + "'" +
            ", lastName='" + lastName + "'" +
            ", email='" + email + "'" +
            "}";
    }

    public boolean isAdmin() {
        return email != null && email.contains(Constants.ADMIN_EMAIL);
    }
}
