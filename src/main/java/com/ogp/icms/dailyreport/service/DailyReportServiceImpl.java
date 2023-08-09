package com.ogp.icms.dailyreport.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ogp.icms.attachment.domain.UploadFile;
import com.ogp.icms.dailyreport.dao.DailyReportJpaRepository;
import com.ogp.icms.dailyreport.dao.DailyReportRepository;
import com.ogp.icms.dailyreport.dao.MonitoringRepository;
import com.ogp.icms.dailyreport.domain.DailyReport;
import com.ogp.icms.dailyreport.domain.Monitoring;
import com.ogp.icms.dailyreport.request.DailyReportSearchCondition;
import com.ogp.icms.dailyreport.request.DailyReportSubmitRequest;
import com.ogp.icms.global.util.ResultCode;
import com.ogp.icms.member.domain.AuthRequest;
import com.ogp.icms.member.domain.Member;
import com.ogp.icms.member.service.MemberServiceImpl;
import com.ogp.icms.schedule.domain.Schedule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class DailyReportServiceImpl {
    private final DailyReportRepository dailyReportRepository;
    private final DailyReportJpaRepository dailyReportJpaRepository;
    private final MonitoringRepository monitoringRepository;
    private final MemberServiceImpl memberService;
    private final ObjectMapper om;

    public Page<DailyReport> getList(DailyReportSearchCondition searchCondition, Pageable pageable, AuthRequest authRequest) {
        if(searchCondition.getGrade() > 1) {
            searchCondition.setUserid(authRequest.getAuthKey());
        }

        int page = pageable.getPageNumber()-1;
        PageRequest newPageRequest = PageRequest.of((page <= 0) ? 0 : page, 10);
        return dailyReportJpaRepository.getPage(searchCondition, newPageRequest);
    }

    public List<DailyReport> getListByDate(String date) {
        System.out.println("date = " + date);
        return dailyReportRepository.findByWorkDateFrom(date);
    }

    public ResultCode addDailyReport(DailyReport dailyReport) {
        //log.info("{} {} {}", dailyReport.getWorkGubun(), dailyReport.getUserid(), dailyReport.getWorkDateFrom());
        DailyReport dailyReport1 = dailyReportRepository.findByWorkGubunAndUseridAndWorkDateFrom(
                dailyReport.getWorkGubun(), dailyReport.getUserid(), dailyReport.getWorkDateFrom());
        if(dailyReport1 != null) return new ResultCode(10, "이미 근무 일지를 작성하였습니다.");
        dailyReport.setStatus(0L);
        dailyReportRepository.save(dailyReport);
        return new ResultCode(0, "근무 일지를 작성하였습니다.");
    }

    public ResultCode modify(Long id, DailyReport dailyReport) {
        DailyReport old = dailyReportRepository.getById(id);

        // 새로 작성한 일지만 수정 가능.
        if(old.getStatus() > 0) return new ResultCode(-10, "결재중이거나 결재를 마친 일지는 수정 및 삭제할 수 없습니다.");

        old.setWorkGubun(dailyReport.getWorkGubun());
        old.setTeam(dailyReport.getTeam());
        old.setWorkDateFrom(dailyReport.getWorkDateFrom());
        old.setWorkDateTo(dailyReport.getWorkDateTo());
        old.setWorkTimeFrom(dailyReport.getWorkTimeFrom());
        old.setWorkTimeTo(dailyReport.getWorkTimeTo());
        old.setWorkMonitoring(dailyReport.getWorkMonitoring());

        return new ResultCode(0, "근무 일지를 수정하였습니다.");
    }
    
    public ResultCode delete(Long id) {
        DailyReport old = dailyReportRepository.getById(id);

        if(old.getStatus() > 0) {
            return new ResultCode(-10, "결재중이거나 결재를 마친 일지는 수정 및 삭제할 수 없습니다.");
        }

        dailyReportRepository.deleteById(id);
        return new ResultCode(0, "근무 일지를 삭제하였습니다.");
    }

    /**
     * 근무 일지 제출하기
     */
    public ResultCode submitDailyReport(DailyReportSubmitRequest submitRequest) {
        dailyReportJpaRepository.submitDailyReport(submitRequest);
        return new ResultCode(0, "근무 일지를 제출하였습니다.");
    }

    /**
     * 근무 일지 결재하기
     */
    public ResultCode confirmDailyReport(List<Long> list, String confirmUserid) {
        for (Long id: list) {
            DailyReport savedDailyReport = dailyReportRepository.getById(id);

            savedDailyReport.setStatus(2L);
            savedDailyReport.setConfirmUserid(confirmUserid);
            savedDailyReport.setConfirmDate(LocalDateTime.now());
        }

        return new ResultCode(0, list.size() + "개의 근무 일지를 결재하였습니다.");
    }

    /**
     * 모니터링에 따라 업데이트
     */
    public void updateDailyReport(Monitoring monitoring) {
        ObjectNode objectNode = om.createObjectNode();
        DailyReport dailyReport = dailyReportRepository.getById(monitoring.getDailyreportId());
        if(dailyReport == null) return;
        List<Monitoring> monitoringList = monitoringRepository.getByDailyreportId(monitoring.getDailyreportId());

        // 3. count by cases
        for (Monitoring item : monitoringList ) {
            String workDgubun = item.getWorkDgubun();
            //log.info("workDgubun : {}", workDgubun);

            int count = 0;
            // 1). action code
            if(hasText(workDgubun)) {
                count = objectNode.has(workDgubun) ? objectNode.get(workDgubun).asInt() : 0;
                objectNode.put(workDgubun, ++count);
            }
        }
        dailyReport.setSummary(objectNode.toString());

        dailyReportRepository.save(dailyReport);
    }

    public int getNotConfirmedCount() {
        return dailyReportRepository.countByStatus(1);
    }

    public ResultCode migration() {

        List<DailyReport> dailyReportList = dailyReportRepository.findBySummaryIsNull();
        int count = 0;

        for (int i = 0; i < dailyReportList.size(); i++) {
            DailyReport dailyReport = dailyReportList.get(i);

            ObjectNode objectNode = om.createObjectNode();
            List<Monitoring> monitoringList = monitoringRepository.getByDailyreportId(dailyReport.getId());

            // 3. count by cases
            for (Monitoring item : monitoringList ) {
                String workDgubun = item.getWorkDgubun();
                //log.info("workDgubun : {}", workDgubun);

                // 1). action code
                if(hasText(workDgubun)) {
                    count = objectNode.has(workDgubun) ? objectNode.get(workDgubun).asInt() : 0;
                    objectNode.put(workDgubun, ++count);
                }
            }
            dailyReport.setSummary(objectNode.toString());

            // connection timeout 방지를 위해 중간에 세이브
            if(i > 1 && i % 10 == 0) {
                log.info("migration progress : {}/{}", i, dailyReportList.size());
            }

            if(i > 1 && i % 1000 == 0) {
                count = i;
                dailyReportRepository.flush();
            }
        }
        return new ResultCode(0, count + "개 완료");
    }

    public DailyReport getById(Long id) {
        DailyReport byId = dailyReportRepository.getById(id);
        return byId;
    }
}
