package com.devoteam.skillshapes.service.dto;


import io.quarkus.runtime.annotations.RegisterForReflection;
import io.swagger.annotations.ApiModel;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.devoteam.skillshapes.domain.ProfileSkillValue} entity.
 */
@ApiModel(description = "The ProfileSkill represents the rating of the skill for each user\n@author Devoteam")
@RegisterForReflection
public class ProfileSkillValueDTO implements Serializable {

    public Long id;

    @NotNull
    @Min(value = 1)
    @Max(value = 4)
    public Integer value;

    public Long skillId;

    public String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfileSkillValueDTO)) {
            return false;
        }

        return id != null && id.equals(((ProfileSkillValueDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ProfileSkillValueDTO{" +
            "id=" + id +
            ", value=" + value +
            ", skillId=" + skillId +
            "}";
    }
}
