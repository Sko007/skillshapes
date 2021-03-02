package com.devoteam.skillshapes.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.devoteam.skillshapes.TestUtil;

public class SkillDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SkillDTO.class);
        SkillDTO skillDTO1 = new SkillDTO();
        skillDTO1.id = 1L;
        SkillDTO skillDTO2 = new SkillDTO();
        assertThat(skillDTO1).isNotEqualTo(skillDTO2);
        skillDTO2.id = skillDTO1.id;
        assertThat(skillDTO1).isEqualTo(skillDTO2);
        skillDTO2.id = 2L;
        assertThat(skillDTO1).isNotEqualTo(skillDTO2);
        skillDTO1.id = null;
        assertThat(skillDTO1).isNotEqualTo(skillDTO2);
    }
}
