package com.ogp.icms.visit.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/visit")
public class VisitController {

    @GetMapping("/list")
    public String list() {
        return "visit/list";
    }

    @GetMapping("/write")
    public String write() {
        return "visit/write";
    }

    @GetMapping("/edit")
    public String edit() {
        return "visit/edit";
    }

    @GetMapping("/out")
    public String out() {
        return "visit/out";
    }
}
