package com.devoteam.skillshapes.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.devoteam.skillshapes.TestUtil;

public class SkillShapeDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SkillShapeDTO.class);
        SkillShapeDTO skillShapeDTO1 = new SkillShapeDTO();
        skillShapeDTO1.id = 1L;
        SkillShapeDTO skillShapeDTO2 = new SkillShapeDTO();
        assertThat(skillShapeDTO1).isNotEqualTo(skillShapeDTO2);
        skillShapeDTO2.id = skillShapeDTO1.id;
        assertThat(skillShapeDTO1).isEqualTo(skillShapeDTO2);
        skillShapeDTO2.id = 2L;
        assertThat(skillShapeDTO1).isNotEqualTo(skillShapeDTO2);
        skillShapeDTO1.id = null;
        assertThat(skillShapeDTO1).isNotEqualTo(skillShapeDTO2);
    }
}
