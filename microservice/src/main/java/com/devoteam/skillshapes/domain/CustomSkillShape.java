package com.devoteam.skillshapes.domain;

import io.quarkus.hibernate.orm.panache.PanacheQuery;

public class CustomSkillShape extends SkillShape {
    public static PanacheQuery<SkillShape> findAllWithEagerRelationshipsByUserProfileId(Long ownerId) {
        return find("select distinct skillShape from SkillShape skillShape left join fetch skillShape.skills left join fetch skillShape.owners owner where owner.id =?1", ownerId);
    }
}
