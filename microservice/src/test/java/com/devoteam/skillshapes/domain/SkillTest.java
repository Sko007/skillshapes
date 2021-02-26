package com.devoteam.skillshapes.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.devoteam.skillshapes.TestUtil;
import org.junit.jupiter.api.Test;


public class SkillTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Skill.class);
        Skill skill1 = new Skill();
        skill1.id = 1L;
        Skill skill2 = new Skill();
        skill2.id = skill1.id;
        assertThat(skill1).isEqualTo(skill2);
        skill2.id = 2L;
        assertThat(skill1).isNotEqualTo(skill2);
        skill1.id = null;
        assertThat(skill1).isNotEqualTo(skill2);
    }
}
