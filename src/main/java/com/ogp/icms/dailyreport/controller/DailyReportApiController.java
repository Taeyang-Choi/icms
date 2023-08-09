package com.ogp.icms.dailyreport.controller;

import com.ogp.icms.dailyreport.domain.DailyReport;
import com.ogp.icms.dailyreport.request.DailyReportSearchCondition;
import com.ogp.icms.dailyreport.request.DailyReportSubmitRequest;
import com.ogp.icms.dailyreport.service.DailyReportServiceImpl;
import com.ogp.icms.global.util.ResultCode;
import com.ogp.icms.member.domain.AuthRequest;
import com.ogp.icms.schedule.service.ScheduleServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@Controller
@RequiredArgsConstructor
public class DailyReportApiController {
    private final DailyReportServiceImpl dailyReportService;
    private final ScheduleServiceImpl scheduleService;

    @GetMapping("/api/daily-reports")
    public Page<DailyReport> getPage(DailyReportSearchCondition searchCondition, Pageable pageable, AuthRequest authRequest) {
        return dailyReportService.getList(searchCondition, pageable, authRequest);
    }

    @GetMapping("/api/daily-reports/{id}")
    public DailyReport getOne(@PathVariable Long id) {
        DailyReport dailyReport = dailyReportService.getById(id);
        Hibernate.initialize(dailyReport); // 프록시 객체를 초기화
        return dailyReport;
    }

    @GetMapping("/api/daily-reports/date/{date}")
    public List<DailyReport> getPage(@PathVariable String date) {
        return dailyReportService.getListByDate(date);
    }

    // 작성
    @PostMapping("/api/daily-reports")
    public ResultCode addDailyReport(@ModelAttribute DailyReport dailyReport) {
        return dailyReportService.addDailyReport(dailyReport);
    }

    @PutMapping("/api/daily-reports/{id}")
    public ResultCode modifyDailyReport(@PathVariable Long id, @ModelAttribute DailyReport dailyReport) {
        return dailyReportService.modify(id, dailyReport);
    }

    @DeleteMapping("/api/daily-reports/{id}")
    public ResultCode deleteDailyReport(@PathVariable Long id) {
        return dailyReportService.delete(id);
    }


    /**
     * 근무 일지 제출
     */
    @PutMapping("/api/daily-reports/{id}/submit")
    public ResultCode submit(@ModelAttribute DailyReportSubmitRequest submitRequest) {
        scheduleService.submitDailyReport(submitRequest.getSubmitUserid());
        return dailyReportService.submitDailyReport(submitRequest);
    }

    @GetMapping("/api/aabbcc")
    public void submit2() {
        scheduleService.submitDailyReport("admin");
    }

    /**
     * 근무 일지 결재
     */
    @PutMapping("/api/daily-reports/confirm")
    public ResultCode confirmDailyReports(@RequestParam(required = false) List<Long> list, String confirmUserid) {
        if(list == null) list = new ArrayList<Long>();

        return dailyReportService.confirmDailyReport(list, confirmUserid);
    }


    // 미결재 근무일지 목록
    @GetMapping("/api/daily-reports/not-confirmed")
    public int notConfirmedCount() {
        //log.info("haha - {}", dailyReportService.getNotConfirmedCount());
        return dailyReportService.getNotConfirmedCount();
    }


    @GetMapping("/api/v2/daily-reports/migration")
    public ResultCode migration() {
        return dailyReportService.migration();
    }
}
