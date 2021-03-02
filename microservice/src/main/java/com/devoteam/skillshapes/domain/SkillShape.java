package com.devoteam.skillshapes.domain;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.json.bind.annotation.JsonbTransient;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Optional;

/**
 * The SkillShape class that represents the job and the skills related to it\n@author Devoteam
 */
@Entity
@Table(name = "skill_shape")
@Cacheable
@RegisterForReflection
public class SkillShape extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    public String title;

    @NotNull
    @Column(name = "category", nullable = false)
    public String category;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "skill_shape_skill",
               joinColumns = @JoinColumn(name = "skill_shape_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "skill_id", referencedColumnName = "id"))
    @JsonbTransient
    public Set<ProfileSkillValue> skills = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "skill_shape_owner",
               joinColumns = @JoinColumn(name = "skill_shape_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "owner_id", referencedColumnName = "id"))
    @JsonbTransient
    public Set<UserProfile> owners = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SkillShape)) {
            return false;
        }
        return id != null && id.equals(((SkillShape) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SkillShape{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", category='" + category + "'" +
            "}";
    }

    public SkillShape update() {
        return update(this);
    }

    public SkillShape persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static SkillShape update(SkillShape skillShape) {
        if (skillShape == null) {
            throw new IllegalArgumentException("skillShape can't be null");
        }
        var entity = SkillShape.<SkillShape>findById(skillShape.id);
        if (entity != null) {
            entity.title = skillShape.title;
            entity.category = skillShape.category;
            entity.skills = skillShape.skills;
            entity.owners = skillShape.owners;
        }
        return entity;
    }

    public static SkillShape persistOrUpdate(SkillShape skillShape) {
        if (skillShape == null) {
            throw new IllegalArgumentException("skillShape can't be null");
        }
        if (skillShape.id == null) {
            persist(skillShape);
            return skillShape;
        } else {
            return update(skillShape);
        }
    }

    public static PanacheQuery<SkillShape> findAllWithEagerRelationships() {
        return find("select distinct skillShape from SkillShape skillShape left join fetch skillShape.skills left join fetch skillShape.owners");
    }

    public static Optional<SkillShape> findOneWithEagerRelationships(Long id) {
        return find("select skillShape from SkillShape skillShape left join fetch skillShape.skills left join fetch skillShape.owners where skillShape.id =?1", id).firstResultOptional();
    }

}
