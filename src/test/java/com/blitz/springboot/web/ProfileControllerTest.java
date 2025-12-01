package com.blitz.springboot.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

public class ProfileControllerTest {

    @Test
    public void profile은_인증없이_호출된다() {
        //given
        String expectedProfile = "test";
        MockEnvironment env = new MockEnvironment();
        env.addActiveProfile(expectedProfile);

        ProfileController controller = new ProfileController(env);

        //when
        ResponseEntity<String> response = controller.profile();

        //then
        assertThat(response.getBody()).isEqualTo(expectedProfile);
    }
}
