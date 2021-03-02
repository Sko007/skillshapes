package com.devoteam.skillshapes.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.devoteam.skillshapes.TestUtil;

public class ProfileSkillValueDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfileSkillValueDTO.class);
        ProfileSkillValueDTO profileSkillValueDTO1 = new ProfileSkillValueDTO();
        profileSkillValueDTO1.id = 1L;
        ProfileSkillValueDTO profileSkillValueDTO2 = new ProfileSkillValueDTO();
        assertThat(profileSkillValueDTO1).isNotEqualTo(profileSkillValueDTO2);
        profileSkillValueDTO2.id = profileSkillValueDTO1.id;
        assertThat(profileSkillValueDTO1).isEqualTo(profileSkillValueDTO2);
        profileSkillValueDTO2.id = 2L;
        assertThat(profileSkillValueDTO1).isNotEqualTo(profileSkillValueDTO2);
        profileSkillValueDTO1.id = null;
        assertThat(profileSkillValueDTO1).isNotEqualTo(profileSkillValueDTO2);
    }
}
