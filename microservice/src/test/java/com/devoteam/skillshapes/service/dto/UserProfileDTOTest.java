package com.devoteam.skillshapes.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.devoteam.skillshapes.TestUtil;

public class UserProfileDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserProfileDTO.class);
        UserProfileDTO userProfileDTO1 = new UserProfileDTO();
        userProfileDTO1.id = 1L;
        UserProfileDTO userProfileDTO2 = new UserProfileDTO();
        assertThat(userProfileDTO1).isNotEqualTo(userProfileDTO2);
        userProfileDTO2.id = userProfileDTO1.id;
        assertThat(userProfileDTO1).isEqualTo(userProfileDTO2);
        userProfileDTO2.id = 2L;
        assertThat(userProfileDTO1).isNotEqualTo(userProfileDTO2);
        userProfileDTO1.id = null;
        assertThat(userProfileDTO1).isNotEqualTo(userProfileDTO2);
    }
}
