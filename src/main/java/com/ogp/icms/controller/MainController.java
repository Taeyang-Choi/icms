package com.ogp.icms.controller;

import com.ogp.icms.global.manager.SessionManager;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class MainController {
    private final SessionManager sessionManager;

    public MainController(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @GetMapping("")
    public String root() {
        return "index";
    }

    @GetMapping("/login")
    public String login(){ return "login";}

    @GetMapping("/dashboard")
    public String dashboard(){ return "dashboard";}

    /**
     * 세션 관리
     */
    @GetMapping("/sesseion/{id}")
    public @ResponseBody String getSession(HttpSession session, @PathVariable String id, @RequestParam String accessToken, @RequestParam String refreshToken) {
        JSONObject sesobj = null;

        try {
            sesobj = sessionManager.get(session, id, accessToken, refreshToken);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (sesobj == null) ? "{}" : sesobj.toString();
    }

    @PostMapping("/sesseion/{id}")
    public @ResponseBody void editSession(HttpSession session, @PathVariable String id, @RequestParam String accessToken, @RequestParam String refreshToken) {

    }
}
