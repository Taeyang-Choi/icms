package com.ogp.icms.schedule.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.ogp.icms.global.manager.SessionManager;
import com.ogp.icms.global.util.ResultCode;
import com.ogp.icms.member.domain.Member;
import com.ogp.icms.schedule.domain.*;
import com.ogp.icms.schedule.service.ScheduleServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class ScheduleApiController {
    private final ScheduleServiceImpl scheduleService;
    private final SessionManager sessionManager;

    //region 출근부

    //TODO : 나중에 세션 정보로 차별 로딩 해야함
    @GetMapping("/api/schedules/monthly/{dateCode}")
    public List<Schedule> getMonthlyAttendance(@PathVariable String dateCode) {
        return scheduleService.getList(dateCode);
    }

    @GetMapping("/api/schedules/monthly/{dateCode}/{id}")
    public List<Schedule> getMonthlyAttendanceById(@PathVariable String dateCode, @PathVariable String id) {
        return scheduleService.getList(dateCode, id);
    }

    @GetMapping("/api/schedules/{id}")
    public Schedule getSchedule(@PathVariable Long id) {
        return scheduleService.get(id);
    }

    
    // 출근부 등록
    @PostMapping("/api/schedules")
    public ResultCode addSchedule(@ModelAttribute Schedule schedule) {
        schedule.setSDate(LocalDateTime.now());
        return scheduleService.save(schedule);
    }

    @PostMapping("/api/schedules/edit")
    public ResultCode editSchedule(@ModelAttribute Schedule schedule) {
        return scheduleService.edit(schedule);
    }


    @PutMapping("/api/schedules")
    public ResultCode dailyReportPost(@ModelAttribute Schedule schedule) {
        return scheduleService.edit(schedule);
    }

    @DeleteMapping("/api/schedules/{id}")
    public ResultCode deleteSchedule(@PathVariable Long id) {
        return scheduleService.delete(id);
    }
    //endregion

    // region 근무 현황
    // 팀 정보 가져오기
    @GetMapping("/api/schedule/main")
    public String getTeam() {
        return "";
    }

    // 모니터 요원 리스트 가져오기
    @GetMapping("/api/schedule/agents")
    public List<Member> getMonitorAgentList() {
        return scheduleService.getMonitorAgentList("3");
    }

    // 팀원 수정
    @PutMapping("/api/schedule/teams/{teamCode}")
    public ResultCode modifyTeamMember(@PathVariable String teamCode, @RequestBody JsonNode data) {
        return scheduleService.modifyTeamMember(teamCode, data);
    }
    // endregion

    //region 시프트 설정
    @PostMapping("/api/schedule/shifts")
    public ResultCode addScheduleShift(@RequestBody ScheduleShift scheduleShift) {
        return scheduleService.addScheduleShift(scheduleShift);
    }

    @GetMapping("/api/schedule/shifts")
    public List<ScheduleShift> getScheduleShiftList() {
        return scheduleService.getScheduleShiftList();
    }

    @DeleteMapping("/api/schedule/shifts/{id}")
    public ResultCode deleteScheduleShift(@PathVariable Long id) {
        return scheduleService.deleteScheduleShift(id);
    }

    //endregion

    //region 패턴 설정
    // 패턴 추가
    @PostMapping("/api/schedules/patterns")
    public ResultCode addSchedulePattern(@ModelAttribute SchedulePattern schedulePattern) {
        return scheduleService.addSchedulePattern(schedulePattern);
    }

    @GetMapping("/api/schedules/patterns")
    public List<SchedulePattern> getSchedulePatternList() {
        return scheduleService.getSchedulePatternList();
    }

    @DeleteMapping("/api/schedule/patterns/{id}")
    public ResultCode deleteSchedulePattern(@PathVariable Long id) {
        return scheduleService.deleteSchedulePattern(id);
    }

    @PutMapping("/api/schedules/patterns/{id}/list")
    public ResultCode updatePatternList(@PathVariable Long id, String list) {
        return scheduleService.updatePatternList(id, list);
    }
    //endregion

    //region 팀 스케줄 설정
    @GetMapping("/api/teams/schedules/{date}")
    public List<TeamSchedule> getAllTeamSchedules(@PathVariable String date) {
        return scheduleService.getAllTeamSchedules(date);
    }

    @GetMapping("/api/teams/schedules/{team}/{date}")
    public TeamSchedule getTeamScheduleByDate(@PathVariable String team, @PathVariable String date) {
        //log.info("{}, {}", team, date);
        return scheduleService.getTeamScheduleByDate(team, date);
    }

    @PostMapping("/api/teams/schedules")
    public ResultCode updateTeamSchedule(@RequestBody JsonNode teamSchedule) {
        return scheduleService.saveTeamSchedule(teamSchedule);
    }
    //endregion

    @GetMapping("/api/teams/schedules/dummy")
    public void getAllTeamSchedules() {
        scheduleService.dummyData();
    }
    
    // 버전 2 팀스케줄
    @PostMapping("/api/v2/schedules")
    public ResultCode setScheduleV2(@ModelAttribute TeamScheduleV2 teamSchedule) {
        return scheduleService.updateTeamSchedule(teamSchedule);
    }
    @PostMapping("/api/v2/schedules/all")
    public ResultCode setScheduleV2All(@ModelAttribute TeamScheduleV2 teamSchedule) {
        return scheduleService.updateTeamScheduleAll(teamSchedule);
    }

    @GetMapping("/api/v2/schedules/migration")
    public ResultCode migration() {
        return scheduleService.migration();
    }
}
