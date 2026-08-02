package com.reduceco2now.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.Map;

@RestController
class MeController {

    @GetMapping("/api/v1/me")
    Map<String, Object> me(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> result = new HashMap<>();
        result.put("subject", jwt.getSubject());
        result.put("email", jwt.getClaimAsString("email"));
        result.put("username", jwt.getClaimAsString("preferred_username"));
        return result;
    }
}