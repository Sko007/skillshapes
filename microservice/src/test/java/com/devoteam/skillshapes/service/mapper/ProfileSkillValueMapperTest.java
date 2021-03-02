package com.devoteam.skillshapes.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ProfileSkillValueMapperTest {

    private ProfileSkillValueMapper profileSkillValueMapper;

    @BeforeEach
    public void setUp() {
        profileSkillValueMapper = new ProfileSkillValueMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(profileSkillValueMapper.fromId(id).id).isEqualTo(id);
        assertThat(profileSkillValueMapper.fromId(null)).isNull();
    }
}
