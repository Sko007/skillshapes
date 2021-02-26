package com.devoteam.skillshapes.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.devoteam.skillshapes.TestUtil;
import org.junit.jupiter.api.Test;


public class ProfileSkillValueTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfileSkillValue.class);
        ProfileSkillValue profileSkillValue1 = new ProfileSkillValue();
        profileSkillValue1.id = 1L;
        ProfileSkillValue profileSkillValue2 = new ProfileSkillValue();
        profileSkillValue2.id = profileSkillValue1.id;
        assertThat(profileSkillValue1).isEqualTo(profileSkillValue2);
        profileSkillValue2.id = 2L;
        assertThat(profileSkillValue1).isNotEqualTo(profileSkillValue2);
        profileSkillValue1.id = null;
        assertThat(profileSkillValue1).isNotEqualTo(profileSkillValue2);
    }
}
