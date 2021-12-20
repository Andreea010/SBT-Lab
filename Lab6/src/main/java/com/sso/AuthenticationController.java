package com.sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthenticationController {
    @RequestMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        Map<String, Object> output = new HashMap<>();
        output.put("name",principal.getAttribute("name"));
        output.put("login",principal.getAttribute("login"));
        output.put("all",principal.getAttributes());
        return output;
    }
}