package com.ogp.icms.dailyreport.controller;

import com.ogp.icms.dailyreport.domain.Monitoring;
import com.ogp.icms.dailyreport.request.MonitoringActionRequest;
import com.ogp.icms.dailyreport.request.MonitoringSearchCondition;
import com.ogp.icms.dailyreport.service.MonitoringServiceImpl;
import com.ogp.icms.global.entity.SearchCondition;
import com.ogp.icms.global.util.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@Controller
@RequiredArgsConstructor
public class MonitoringApiController {
    private final MonitoringServiceImpl monitoringService;

    @GetMapping("/api/daily-reports/events")
    public Page<Monitoring> getDailyReportDetailList(MonitoringSearchCondition searchCondition, Pageable pageable) {
        return monitoringService.getList(searchCondition, pageable);
    }

    @GetMapping("/api/daily-reports/setWorkMonitoring")
    public ResultCode setWorkMonitoring() {
        return monitoringService.setWorkMonitoring();
    }

    @GetMapping("/api/daily-reports/events/date/{date}")
    public List<Monitoring> getPage(@PathVariable String date) {
        return monitoringService.findByDate(date);
    }

    // 특정 일지의 모니터링 내역을 가져온다.
    @GetMapping("/api/daily-reports/{reportId}/events")
    public List<Monitoring> getDailyReportDetailList(@PathVariable Long reportId) {
        return monitoringService.getDailyEvents(reportId);
    }

    // 특정 id의 모니터링을 가져온다.
    @GetMapping("/api/daily-reports/events/{id}")
    public Monitoring getMonitoringEvent(@PathVariable Long id) {
        return monitoringService.getMonitoringEvent(id);
    }

    // 모니터링 등록
    @PostMapping("/api/daily-reports/events")
    public ResultCode addDailyReportDetail(@ModelAttribute Monitoring monitoring) {

        return monitoringService.addDailyReport(monitoring);
    }

    @PutMapping("/api/daily-reports/events/{id}")
    public ResultCode modifyDailyReportDetail(@PathVariable Long id, @ModelAttribute Monitoring monitoring) {
        return monitoringService.modify(id, monitoring);
    }

    @DeleteMapping("/api/daily-reports/events/{id}")
    public ResultCode deleteDailyReportDetail(@PathVariable Long id) {
        return monitoringService.delete(id);
    }

    //region 모니터링 조치 관련
    @PutMapping("/api/daily-reports/events/{id}/action")
    public ResultCode actionDailyReportEvent(@PathVariable Long id, @ModelAttribute MonitoringActionRequest actionRequest) {
        return monitoringService.addAction(id, actionRequest);
    }
    //endregion

    // 엑셀용 모든 모니터링
    @GetMapping("/api/daily-reports/events/excel")
    public List<Monitoring> getAllMonitoringList(@ModelAttribute MonitoringSearchCondition searchCondition) {
        return monitoringService.getAll(searchCondition);
    }

    // 특정 카메라 사건
    @GetMapping("/api/daily-reports/cameras/{id}/events")
    public List<Monitoring> getAllMonitoringList(@PathVariable Long id) {
        return monitoringService.getByCctvId(id);
    }

    @GetMapping("/api/daily-reports/events/errors")
    public List<Monitoring> getAllNotRepairedAssets() {
        return monitoringService.getAllNotRepairedAssets();
    }


    @GetMapping("/api/daily-reports/events/situs")
    public List<Monitoring> getAllSituations() {
        return monitoringService.getAllSituations();
    }
}
