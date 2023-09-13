package com.ogp.icms.controller;

import com.ogp.icms.dailyreport.service.DailyReportServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/dailyreport")
public class DailyReportController {
    private final DailyReportServiceImpl dailyReportService;

    @GetMapping("/list")
    public String list() {
        return "dailyreport/list";
    }

    @GetMapping("/write")
    public String write() {
        return "dailyreport/write";
    }

    @GetMapping("/edit")
    public String edit() {
        return "dailyreport/edit";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id) {
        return "dailyreport/detail";
    }

    @GetMapping("/excel")
    public String excel() {return "dailyreport/excel";}

    @GetMapping("/event/write")
    public String eventWrite() {return "dailyreport/ver1/event/write";}

    @GetMapping("/event/edit")
    public String eventEdit() {return "dailyreport/ver1/event/edit";}

    @GetMapping("/event/write2")
    public String eventWrite2() {return "dailyreport/ver2/event/write";}
    @GetMapping("/event/edit2")
    public String eventEdit2() {return "dailyreport/ver2/event/edit";}
}
