package com.devoteam.skillshapes.service.dto;


import io.quarkus.runtime.annotations.RegisterForReflection;
import io.swagger.annotations.ApiModel;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.devoteam.skillshapes.domain.Skill} entity.
 */
@ApiModel(description = "The Skill class representing a single skill with its name, category and rating (stars)\n@author Devoteam")
@RegisterForReflection
public class SkillDTO implements Serializable {
    
    public Long id;

    @NotNull
    public String name;

    @NotNull
    public String categoryName;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SkillDTO)) {
            return false;
        }

        return id != null && id.equals(((SkillDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SkillDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", categoryName='" + categoryName + "'" +
            "}";
    }
}
