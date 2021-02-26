package com.devoteam.skillshapes.domain;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.json.bind.annotation.JsonbTransient;
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
import java.util.Optional;

/**
 * The Skill class representing a single skill with its name, category and rating (stars)\n@author Devoteam
 */
@ApiModel(description = "The Skill class representing a single skill with its name, category and rating (stars)\n@author Devoteam")
@Entity
@Table(name = "skill")
@Cacheable
@RegisterForReflection
public class Skill extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    public String name;

    @NotNull
    @Column(name = "category_name", nullable = false)
    public String categoryName;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "skill_owner",
               joinColumns = @JoinColumn(name = "skill_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "owner_id", referencedColumnName = "id"))
    @JsonbTransient
    public Set<UserProfile> owners = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Skill)) {
            return false;
        }
        return id != null && id.equals(((Skill) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Skill{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", categoryName='" + categoryName + "'" +
            "}";
    }

    public Skill update() {
        return update(this);
    }

    public Skill persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Skill update(Skill skill) {
        if (skill == null) {
            throw new IllegalArgumentException("skill can't be null");
        }
        var entity = Skill.<Skill>findById(skill.id);
        if (entity != null) {
            entity.name = skill.name;
            entity.categoryName = skill.categoryName;
            entity.owners = skill.owners;
        }
        return entity;
    }

    public static Skill persistOrUpdate(Skill skill) {
        if (skill == null) {
            throw new IllegalArgumentException("skill can't be null");
        }
        if (skill.id == null) {
            persist(skill);
            return skill;
        } else {
            return update(skill);
        }
    }

    public static PanacheQuery<Skill> findAllWithEagerRelationships() {
        return find("select distinct skill from Skill skill left join fetch skill.owners");
    }

    public static Optional<Skill> findOneWithEagerRelationships(Long id) {
        return find("select skill from Skill skill left join fetch skill.owners where skill.id =?1", id).firstResultOptional();
    }

}
