package com.devoteam.skillshapes.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.devoteam.skillshapes.TestUtil;
import org.junit.jupiter.api.Test;


public class SkillShapeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SkillShape.class);
        SkillShape skillShape1 = new SkillShape();
        skillShape1.id = 1L;
        SkillShape skillShape2 = new SkillShape();
        skillShape2.id = skillShape1.id;
        assertThat(skillShape1).isEqualTo(skillShape2);
        skillShape2.id = 2L;
        assertThat(skillShape1).isNotEqualTo(skillShape2);
        skillShape1.id = null;
        assertThat(skillShape1).isNotEqualTo(skillShape2);
    }
}
