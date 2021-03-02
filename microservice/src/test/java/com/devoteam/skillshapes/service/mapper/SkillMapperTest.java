package com.devoteam.skillshapes.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SkillMapperTest {

    private SkillMapper skillMapper;

    @BeforeEach
    public void setUp() {
        skillMapper = new SkillMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(skillMapper.fromId(id).id).isEqualTo(id);
        assertThat(skillMapper.fromId(null)).isNull();
    }
}
