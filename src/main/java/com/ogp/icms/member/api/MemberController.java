package com.ogp.icms.member.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ogp.icms.global.manager.SessionManager;
import com.ogp.icms.member.domain.Member;
import com.ogp.icms.member.service.MemberServiceImpl;
import com.ogp.icms.global.util.ResultCode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@RestController
@Transactional
@AllArgsConstructor
public class MemberController {
    private final SessionManager sessionManager;
    private final MemberServiceImpl memberService;

    //region 로그인
    @PostMapping("/api/login")
    public ResultCode login(@RequestParam String id, @RequestParam String pass, HttpSession session) throws Exception {
        Member member = memberService.login(id, pass);

        // 로그인 실패
        if(member == null) return new ResultCode(-99, "아이디나 비밀번호가 정확하지 않습니다.");
        log.info("logged in user={}", id);

        ObjectMapper objectMapper = new ObjectMapper();

        // 로그인 성공
        sessionManager.put(session, id, "", "", "usrobj", new JSONObject());
        return new ResultCode(0, "인증 성공").data(member);
    }

    @DeleteMapping("/api/logout")
    public ResultCode logout(HttpSession session) throws Exception {
        sessionManager.remove(session, "id", "usrobj", "acc", "ref");

        return new ResultCode(0, "로그아웃");
    }
    //endregion

    //region 멤버 관리
    // 멤버 삽입
    @PostMapping("/api/members")
    public ResultCode addMember(@ModelAttribute Member member) {
        return memberService.save(member);
    }

    // 멤버 얻기
    @GetMapping("/api/members")
    public List<Member> getAll() {
        return memberService.getAllMembers();
    }

    // 특정 멤버 정보
    @GetMapping("/api/members/{id}")
    public Member findMember(@PathVariable Long id) {
        return memberService.findById(id);
    }

    // 멤버수정
    @PutMapping("/api/members/{id}")
    public ResultCode editMember(@PathVariable Long id, @ModelAttribute Member member) {
        return memberService.modify(id, member);
    }

    // 멤버 삭제
    @DeleteMapping("/api/members/{id}")
    public ResultCode deleteMemberById(@PathVariable Long id) {
        return memberService.deleteMemberById(id);
    }
    //endregion

    //region 팀 관리
    // 모니터 요원 리스트 가져오기
    @GetMapping("/api/members/grades/{grade}")
    public List<Member> getMonitorAgentList(@PathVariable String grade) {
        return memberService.getListByGrade(grade);
    }

    //endregion
}
