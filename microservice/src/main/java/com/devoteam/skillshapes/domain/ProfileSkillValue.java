package com.devoteam.skillshapes.domain;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.json.bind.annotation.JsonbTransient;
import javax.json.bind.annotation.JsonbProperty;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * The ProfileSkill represents the rating of the skill for each user\n@author Devoteam
 */
@ApiModel(description = "The ProfileSkill represents the rating of the skill for each user\n@author Devoteam")
@Entity
@Table(name = "profile_skill_value")
@Cacheable
@RegisterForReflection
public class ProfileSkillValue extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotNull
    @Min(value = 1)
    @Max(value = 4)
    @Column(name = "value", nullable = false)
    public Integer value;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    @JsonbTransient
    public Skill skill;

    @ManyToMany(mappedBy = "skills")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonbTransient
    public Set<SkillShape> skillshapes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfileSkillValue)) {
            return false;
        }
        return id != null && id.equals(((ProfileSkillValue) o).id);
    }

    @JsonbProperty("skill")
    public String getSkillName(){
        return (skill != null) ? skill.name : "No skill associated";
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ProfileSkillValue{" +
            "id=" + id +
            ", value=" + value +
            ", name=" + skill.name +
            "}";
    }

    public ProfileSkillValue update() {
        return update(this);
    }

    public ProfileSkillValue persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static ProfileSkillValue update(ProfileSkillValue profileSkillValue) {
        if (profileSkillValue == null) {
            throw new IllegalArgumentException("profileSkillValue can't be null");
        }
        var entity = ProfileSkillValue.<ProfileSkillValue>findById(profileSkillValue.id);
        if (entity != null) {
            entity.value = profileSkillValue.value;
            entity.skill = profileSkillValue.skill;
            entity.skillshapes = profileSkillValue.skillshapes;
        }
        return entity;
    }

    public static ProfileSkillValue persistOrUpdate(ProfileSkillValue profileSkillValue) {
        if (profileSkillValue == null) {
            throw new IllegalArgumentException("profileSkillValue can't be null");
        }
        if (profileSkillValue.id == null) {
            persist(profileSkillValue);
            return profileSkillValue;
        } else {
            return update(profileSkillValue);
        }
    }


}
