package com.ogp.icms.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/monitor")
public class MonitoringController {
    @GetMapping("/list")
    public String list() {
        return "monitor/list";
    }

    @GetMapping("/detail")
    public String detail() {
        return "monitor/detail";
    }
}
