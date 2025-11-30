package com.blitz.springboot.web;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/profile")
public class ProfileController {

    private static final List<String> REAL_PROFILES = Arrays.asList("real", "real1", "real2");
    private static final String DEFAULT_PROFILE = "default";

    private final Environment env;

    @GetMapping
    public ResponseEntity<String> profile() {
        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        String defaultProfile = profiles.isEmpty() ? DEFAULT_PROFILE : profiles.get(0);

        String activeProfile = profiles.stream()
                .filter(REAL_PROFILES::contains)
                .findAny()
                .orElse(defaultProfile);

        return ResponseEntity.ok(activeProfile);
    }
}
