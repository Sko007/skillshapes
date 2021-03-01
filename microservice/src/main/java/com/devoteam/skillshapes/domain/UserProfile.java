package com.devoteam.skillshapes.domain;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.json.bind.annotation.JsonbTransient;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Devoteam Profile\n
 */
@ApiModel(description = "Devoteam Profile\n")
@Entity
@Table(name = "user_profile")
@Cacheable
@RegisterForReflection
@Indexed
public class UserProfile extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "first_name")
    @FullTextField(analyzer = "name")
    @KeywordField(name = "firstName_sort", sortable = Sortable.YES, normalizer = "sort")
    public String firstName;

    @Column(name = "last_name")
    @FullTextField(analyzer = "name")
    @KeywordField(name = "lastName_sort", sortable = Sortable.YES, normalizer = "sort")
    public String lastName;

    @Column(name = "email")
    @FullTextField(analyzer = "name")
    public String email;

    @Column(name = "general_knowledge")
    public String generalKnowledge;

    @ManyToMany(mappedBy = "owners")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonbTransient
    public Set<Skill> skills = new HashSet<>();

    @ManyToMany(mappedBy = "owners")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonbTransient
    public Set<SkillShape> skillshapes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserProfile)) {
            return false;
        }
        return id != null && id.equals(((UserProfile) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
            "id=" + id +
            ", firstName='" + firstName + "'" +
            ", lastName='" + lastName + "'" +
            ", email='" + email + "'" +
            ", generalKnowledge='" + generalKnowledge + "'" +
            "}";
    }

    public UserProfile update() {
        return update(this);
    }

    public UserProfile persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static UserProfile update(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("userProfile can't be null");
        }
        var entity = UserProfile.<UserProfile>findById(userProfile.id);
        if (entity != null) {
            entity.firstName = userProfile.firstName;
            entity.lastName = userProfile.lastName;
            entity.email = userProfile.email;
            entity.generalKnowledge = userProfile.generalKnowledge;
            entity.skills = userProfile.skills;
            entity.skillshapes = userProfile.skillshapes;
        }
        return entity;
    }

    public static UserProfile persistOrUpdate(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("userProfile can't be null");
        }
        if (userProfile.id == null) {
            persist(userProfile);
            return userProfile;
        } else {
            return update(userProfile);
        }
    }


}
