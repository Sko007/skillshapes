package com.devoteam.skillshapes.service.dto;


import io.quarkus.runtime.annotations.RegisterForReflection;
import io.swagger.annotations.ApiModel;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the {@link com.devoteam.skillshapes.domain.SkillShape} entity.
 */
@ApiModel(description = "The SkillShape class that represents the job and the skills related to it\n@author Devoteam")
@RegisterForReflection
public class SkillShapeDTO implements Serializable {

    public Long id;

    @NotNull
    public String title;

    @NotNull
    public String category;

    public Set<ProfileSkillValueDTO> skills;
    public Set<UserProfileDTO> owners = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SkillShapeDTO)) {
            return false;
        }

        return id != null && id.equals(((SkillShapeDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SkillShapeDTO{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", category='" + category + "'" +
            ", skills='" + skills + "'" +
            ", owners='" + owners + "'" +
            "}";
    }
}
