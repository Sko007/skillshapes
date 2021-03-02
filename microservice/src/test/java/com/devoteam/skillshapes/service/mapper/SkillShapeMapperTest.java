package com.devoteam.skillshapes.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SkillShapeMapperTest {

    private SkillShapeMapper skillShapeMapper;

    @BeforeEach
    public void setUp() {
        skillShapeMapper = new SkillShapeMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(skillShapeMapper.fromId(id).id).isEqualTo(id);
        assertThat(skillShapeMapper.fromId(null)).isNull();
    }
}
