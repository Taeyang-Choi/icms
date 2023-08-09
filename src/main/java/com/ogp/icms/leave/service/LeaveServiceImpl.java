package com.ogp.icms.leave.service;


import com.ogp.icms.global.util.ResultCode;
import com.ogp.icms.leave.dao.LeaveJpaRepository;
import com.ogp.icms.leave.dao.LeaveRepository;
import com.ogp.icms.leave.dao.LeaveTypeRepository;
import com.ogp.icms.leave.domain.Leave;
import com.ogp.icms.leave.domain.LeaveType;
import com.ogp.icms.leave.request.LeaveSearchCondition;
import com.ogp.icms.member.dao.MemberRepository;
import com.ogp.icms.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class LeaveServiceImpl {
    private final LeaveRepository leaveRepository;
    private final LeaveJpaRepository leaveJpaRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final MemberRepository memberRepository;

    public Page<Leave> getLeavePage(LeaveSearchCondition leaveSearchCondition, Pageable pageable) {
        int page = pageable.getPageNumber()-1;
        PageRequest newPageRequest = PageRequest.of((page <= 0) ? 0 : page, 10);
        return leaveJpaRepository.findByQuery(leaveSearchCondition, newPageRequest);
    }

    public List<Leave> getLeaveList(LeaveSearchCondition leaveSearchCondition, Pageable pageable) {
        int page = pageable.getPageNumber()-1;
        PageRequest newPageRequest = PageRequest.of((page <= 0) ? 0 : page, 1000);
        return leaveJpaRepository.findByQuery(leaveSearchCondition, newPageRequest).getContent();
    }

    /**
     * @TODO optional 수정
     */
    public ResultCode save(Leave leave) {
        Member drafter = memberRepository.findById(leave.getDrafterId()).get();
        LeaveType type = leaveTypeRepository.findById(leave.getTypeId()).get();

        leave.setDept(drafter.getDept());
        leave.setGrade(drafter.getGrade());

        // 휴가 타입 설정
        leave.setTypeType(type.getType());

        leaveRepository.save(leave);
        return new ResultCode(0, "휴가를 신청했습니다.");
    }

    public ResultCode delete(Long id) {
        leaveRepository.deleteById(id);
        return new ResultCode(0, "휴가를 삭제했습니다.");
    }

    public ResultCode modify(LeaveType LeaveType) {
        /*

        savedLeave.setType(LeaveType.getType());
        savedLeave.setActive(LeaveType.getActive());
        savedLeave.setAnnual(LeaveType.getAnnual());
        savedLeave.setName(LeaveType.getName());
        */
        return new ResultCode(0, "휴가 종류를 삭제했습니다.");
    }

    public ResultCode deleteLeave(Long id) {
        leaveRepository.deleteById(id);
        return new ResultCode(0, "휴가를 삭제했습니다.");
    }

    public ResultCode modifyAlternativeWorker(Long id, Long workerId, String workerName) {
        Leave savedLeave = leaveRepository.getById(id);
        savedLeave.setAlternativeId(workerId);
        savedLeave.setAlternativeName(workerName);

        return new ResultCode(0, "대체 근무자를 "+workerName+"(으)로 변경했습니다.");
    }

    public ResultCode modifyStatus(Long id, int status) {
        Leave savedLeave = leaveRepository.getById(id);
        savedLeave.setStatus(status);

        // 1 : 승인, 2 : 거절
        return new ResultCode(0, ((status == 1)? "승인처리가 완료됐습니다.": "거절처리가 완료됐습니다." ));
    }


}
