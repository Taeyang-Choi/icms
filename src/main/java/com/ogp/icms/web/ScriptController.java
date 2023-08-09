package com.ogp.icms.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScriptController {
    private final String configString;
    private final ObjectMapper objectMapper;
    private final Environment environment;
    @Value("${spring.profiles.active}")
    private String activeProfile;

    public ScriptController(ObjectMapper objectMapper, Environment environment) {
        this.objectMapper = objectMapper;
        this.environment = environment;
        this.configString = generateScript();
    }

    @GetMapping("/js/config.js")
    public String getActiveProfile() {
            return "let activeProfile = \"" + activeProfile + "\";";
    }

    private String generateScript() {
        ObjectNode objectNode = objectMapper.createObjectNode();
        String str = "";
        return str;
    }
}
