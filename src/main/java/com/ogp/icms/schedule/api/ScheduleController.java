package com.ogp.icms.schedule.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/schedule")
public class ScheduleController {

    @GetMapping("/calendar/list")
    public String calendarList() {
        return "schedule/calendar/list";
    }

    @GetMapping("/calendar/write")
    public String calendarWrite() {return "schedule/calendar/write";}

    @GetMapping("/calendar/edit")
    public String calendarEdit() {return "schedule/calendar/edit";}


    @GetMapping("/main")
    public String main() {
        return "schedule/main";
    }

    @GetMapping("/monthly")
    public String monthly() {return "schedule/monthly";}

    @GetMapping("/preview")
    public String preview() {return "schedule/preview";}

    @GetMapping("/weekly")
    public String weekly() {return "schedule/weekly";}

    @GetMapping("/write")
    public String write() {return "schedule/write";}
}
