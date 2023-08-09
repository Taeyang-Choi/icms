package com.ogp.icms.schedule.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.ogp.icms.dailyreport.domain.DailyReport;
import com.ogp.icms.global.util.ResultCode;
import com.ogp.icms.member.dao.MemberJpaRepository;
import com.ogp.icms.member.dao.MemberRepository;
import com.ogp.icms.member.domain.Member;
import com.ogp.icms.schedule.dao.*;
import com.ogp.icms.schedule.domain.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class ScheduleServiceImpl {
    private final ScheduleJpaRepository scheduleJpaRepository;
    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final ScheduleShiftRepository scheduleShiftRepository;
    private final SchedulePatternRepository schedulePatternRepository;
    private final TeamScheduleRepository teamScheduleRepository;

    /**
     * 스케줄 저장
     */
    public ResultCode save(Schedule schedule) {
        List<Schedule> scheduleList = getList(schedule.getDatecode(), schedule.getUserwid());
        Boolean existFlag = false;

         // 이미 있나 검사
        for (Schedule s: scheduleList) {
            LocalDate date = s.getSDate().toLocalDate(); // LocalDateTime을 LocalDate로 변환합니다.

            log.info("{}", s.getSDate().getDayOfMonth());
            if(s.getSDate().getDayOfMonth() == schedule.getSDate().getDayOfMonth()) {
                return new ResultCode(0, "이미 등록 돼 있습니다.");
            }
        }
        
        scheduleRepository.save(schedule);
        return new ResultCode(0, "스케줄을 등록했습니다.");
    }

    public Boolean isExist(LocalDateTime localDateTime) {
        return true;
    }

    /**
     * datacode로 리스트 획득
     */
    public List<Schedule> getList(String datecode) {
        return scheduleRepository.findByDatecode(datecode);
    }

    public List<Schedule> getList(String datecode, String id) {
        return scheduleRepository.findByDatecodeAndUserwid(datecode, id);
    }

    /**
     * id로 스케줄 리턴
     */
    public Schedule get(Long id) {
        Optional<Schedule> scheduleOptional = scheduleRepository.findById(id);
        return scheduleOptional.get();
    }

    /**
     * 스케줄 삭제
     */
    public ResultCode delete(Long id) {
         scheduleRepository.deleteById(id);
        return new ResultCode(0,"해당 스케줄을 삭제했습니다.");
    }

    public ResultCode edit(Schedule schedule) {
        Schedule oldSchedule = scheduleRepository.getById(schedule.getId());

        oldSchedule.setTeam(schedule.getTeam());
        oldSchedule.setWorkform(schedule.getWorkform());
        oldSchedule.setDivision(schedule.getDivision());

        scheduleRepository.flush();

        // 작성 해야함
        return new ResultCode(0, "해당 스케줄을 수정했습니다.");
    }

    /**
     * 근무 일지 제출시(퇴근)
     */
    public void submitDailyReport(String submitUserid) {
        Optional<Schedule> scheduleOptional = scheduleRepository.findTop1ByUserwidOrderByIdDesc(submitUserid);
        if(scheduleOptional.isPresent()) {
            Schedule schedule = scheduleOptional.get();
            schedule.setEDate(LocalDateTime.now());
        }
    }


    // ********************************************************
    // 근무 현황 페이지 기능
    // ********************************************************
    public List<Member> getMonitorAgentList(String grade) {
        return memberRepository.findByGrade(grade);
    }

    public void createNewTeam(String jsonObject) {
        //commonDAO.selPut(jsonObject);
    }

    /**
     * 팀 멤버 수정
     */
    public ResultCode modifyTeamMember(String teamCode, JsonNode newMemberIdList) {
        // 1. 기존 팀 멤버 null
        List<Member> teamMembers = memberRepository.findByTeam(teamCode);

        for(Member member : teamMembers) {
            log.info("update1 {}", member);
            member.setTeam("NOTEAM");
        }

        // 2. 새로 추가
        for (int i = 0; i < newMemberIdList.size(); i++) {
            Member member = memberRepository.findById(newMemberIdList.get(i).asLong()).get();
            log.info("update2 {}", member);

            member.setTeam(teamCode);
        }
        return new ResultCode(0, "팀원을 수정했습니다.");
    }

    //region 스케줄 시프트
    public ResultCode addScheduleShift(ScheduleShift scheduleShift) {
        scheduleShiftRepository.save(scheduleShift);
        return new ResultCode(0, "새로운 근무 시간을 추가했습니다.");
    }

    public List<ScheduleShift> getScheduleShiftList() {
        return scheduleShiftRepository.findAll();
    }

    public ResultCode deleteScheduleShift(Long id) {
        scheduleShiftRepository.deleteById(id);
        return new ResultCode(0, "근무 시간을 삭제했습니다.");
    }
    //endregion

    //region 스케줄 패턴
    // 패턴 추가
    public ResultCode addSchedulePattern(SchedulePattern schedulePattern) {
        schedulePatternRepository.save(schedulePattern);
        return new ResultCode(0, schedulePattern.getTeam() + "의 근무 패턴을 추가했습니다.");
    }

    // 패턴 리스트 수정
    public ResultCode updatePatternList(Long id, String list) {
        SchedulePattern oldSchedulePattern = schedulePatternRepository.getById(id);
        oldSchedulePattern.setList(list);

        schedulePatternRepository.flush();
        return new ResultCode(0, oldSchedulePattern.getName() + "의 근무 패턴을 수정했습니다.");
    }

    /**
     * 해당 팀의 스케줄 패턴을 수정한다.
     */
    public ResultCode updateSchedulePattern(SchedulePattern schedulePattern) {
        SchedulePattern oldSchedulePattern = schedulePatternRepository.getById(schedulePattern.getId());
        oldSchedulePattern.setList(schedulePattern.getList());
        log.info("{}", oldSchedulePattern);

        return new ResultCode(0, oldSchedulePattern.getTeam() + "의 근무 패턴을 수정했습니다.");
    }

    public List<SchedulePattern> getSchedulePatternList() {
        return schedulePatternRepository.findAll();
    }

    public ResultCode deleteSchedulePattern(Long id) {
        schedulePatternRepository.deleteById(id);
        return new ResultCode(0, "패턴을 삭제했습니다.");
    }
    //endregion

    //region 팀 스케줄
    public List<TeamSchedule> getAllTeamSchedules(String date) {
        List<TeamSchedule> teamScheduleList = null;
        if(date.length() == 6) {
            teamScheduleList = teamScheduleRepository.findByDateBetween(date+"01", date+"31");
        }
        else {
            teamScheduleList = teamScheduleRepository.findByDate(date);
        }
        return teamScheduleList;
    }

    // 특정 팀의 스케줄
    public TeamSchedule getTeamScheduleByDate(String team, String date) {
        return teamScheduleRepository.findByTeamAndDate(team, date);
    }

    /**
     * 새로운 스케줄을 DB에 저장한다.
     */
    public ResultCode saveTeamSchedule(JsonNode teamSchedules) {
        for(int i = 0; i < teamSchedules.size(); i++) {
            JsonNode temp = teamSchedules.get(i);
            String team = temp.get("team").asText();
            String dateCode = temp.get("dateCode").asText();
            String shift = temp.get("shift").asText();

            Optional<TeamSchedule> optionalTeamSchedule = teamScheduleRepository.findById( new TeamScheduleId(team,dateCode));

            if(optionalTeamSchedule.isPresent()) {
                TeamSchedule oldTeamSchedule = optionalTeamSchedule.get();
                oldTeamSchedule.setShift(shift);
            }
            else {
                TeamSchedule teamSchedule = new TeamSchedule();
                teamSchedule.setTeam(team);
                teamSchedule.setDate(dateCode);
                teamSchedule.setShift(shift);
                teamScheduleRepository.save(teamSchedule);
            }
        }
        teamScheduleRepository.flush();
        return new ResultCode(0, "팀 스케줄을 업데이트 했습니다.");
    }
    //end region

    public void dummyData() {
        List<TeamSchedule> teamScheduleList = teamScheduleRepository.findAll();
        List<Member> memberList = memberRepository.findByGrade("3");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 1. 모든 스케줄 루프
        for(int i = 0; i < teamScheduleList.size(); i++) {
            TeamSchedule teamSchedule = teamScheduleList.get(i);
            if(teamSchedule.getShift().equals("비")) continue;

            String d = teamSchedule.getDate();
            String d1 = d.substring(0,4) + "-" + d.substring(4,6) + "-" + d.substring(6,8) + " ";

            // 2. 모든 멤버 루프
            for (Member member: memberList) {
                if(member.getTeam().equals(teamSchedule.getTeam())) {
                    Schedule schedule = new Schedule();
                    schedule.setDivision(teamSchedule.getShift());
                    schedule.setUserwid(member.getUserid());
                    schedule.setUserwnm(member.getName());
                    schedule.setDatecode(teamSchedule.getDate().substring(0, 6));
                    schedule.setWorkform("01");
                    schedule.setGrade("모니터링요원");
                    schedule.setTeam(member.getTeam());

                    if(teamSchedule.getShift().equals("조")) {
                        schedule.setSDate(LocalDateTime.parse(d1 + "05:50:00",df));
                        schedule.setEDate(LocalDateTime.parse(d1 + "14:10:00",df));
                    }
                    else if(teamSchedule.getShift().equals("주")) {
                        schedule.setSDate(LocalDateTime.parse(d1 + "13:50:00",df));
                        schedule.setEDate(LocalDateTime.parse(d1 + "22:10:00",df));
                    }
                    else if(teamSchedule.getShift().equals("야")) {
                        schedule.setSDate(LocalDateTime.parse(d1 + "21:50:00",df));
                        schedule.setEDate(LocalDateTime.parse(d1 + "06:10:00",df).plusDays(1));
                    }

                    scheduleRepository.save(schedule);
                }
            }
        }
    }

    /**
     * 특정 기간안에 있는 패턴을 이용해서 스케줄을 업데이트 한다.
     * schedule/main.html
     */
    public ResultCode updateTeamScheduleAll(TeamScheduleV2 teamSchedule) {
        // 모든 패턴 불러오기
        List<SchedulePattern> schedulePatternList = getSchedulePatternList();

        for(int i = 0; i < schedulePatternList.size(); i++) {
            SchedulePattern schedulePattern = schedulePatternList.get(i);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate patternStartDate = LocalDate.parse(schedulePattern.getStartDate(), formatter);
            LocalDate patternEndDate = LocalDate.parse(schedulePattern.getEndDate(), formatter);

            // 시작 날짜 비교 (지정한 날짜 이전의 패턴은 무시함)
            int result = teamSchedule.getStartDate().toLocalDate().compareTo(patternStartDate);
            if (result > 0) continue;
            
            // 종료 날짜 비교
            result = teamSchedule.getEndDate().toLocalDate().compareTo(patternEndDate);
            if (result < 0) continue;

            //log.info("pattern {} , result = {}", schedulePattern,result);

            teamSchedule.setPattern(schedulePatternList.get(i).getId());
            updateTeamSchedule(teamSchedule);
        }

        return new ResultCode(0);
    }




    /**
     * 특정 팀의 스케줄을 업데이트한다.
     */
    public ResultCode updateTeamSchedule(TeamScheduleV2 teamSchedule) {
        SchedulePattern pattern = schedulePatternRepository.getById(teamSchedule.getPattern());

        if(pattern.getList() == null) return new ResultCode(0);
        // 패턴 나누기
        String[] patterns = pattern.getList().split(",");
        int index = 0;

        // 날짜
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        List<TeamSchedule> scheduleList = new ArrayList<>();

        // 간단하게 400일 정도 loop
        for(int i = 0; i < 400; i++) {
            // 시프트 코드 가져오기
            String shiftCode = patterns[index];
            LocalDateTime current = teamSchedule.getStartDate().plusDays(i);

            // 지정한 스케줄의 끝까지 가면 break
            if(current.isAfter(teamSchedule.getEndDate())) break;
            if(i % 50 == 0) log.info("day = {}, team = {}, shift = {}", current, pattern.getTeam(), shiftCode);

            // 생성
            TeamSchedule schedule = new TeamSchedule();
            schedule.setShift(shiftCode);
            schedule.setTeam(pattern.getTeam());
            schedule.setDate(current.format(formatter));

            // 리스트에 삽입
            scheduleList.add(schedule);

            // 패턴 index location
            index++;
            if(index == patterns.length) index = 0;
        }

        teamScheduleRepository.saveAll(scheduleList);
        return new ResultCode(0);
    }

    public ResultCode migration() {
        List<Schedule> scheduleList = scheduleRepository.findByDatecode("000000");

        for (Schedule schedule: scheduleList) {
            if(schedule.getSDate() == null) continue;
            schedule.setDatecode(schedule.getSDate().format(DateTimeFormatter.ofPattern("yyyyMM")));
        }
        
        return new ResultCode(0, scheduleList.size() + "개 완료");
    }
}
