package com.devoteam.skillshapes.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.devoteam.skillshapes.TestUtil;
import org.junit.jupiter.api.Test;


public class UserProfileTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserProfile.class);
        UserProfile userProfile1 = new UserProfile();
        userProfile1.id = 1L;
        UserProfile userProfile2 = new UserProfile();
        userProfile2.id = userProfile1.id;
        assertThat(userProfile1).isEqualTo(userProfile2);
        userProfile2.id = 2L;
        assertThat(userProfile1).isNotEqualTo(userProfile2);
        userProfile1.id = null;
        assertThat(userProfile1).isNotEqualTo(userProfile2);
    }
}
