package com.ogp.icms.leave.service;


import com.ogp.icms.global.util.ResultCode;
import com.ogp.icms.leave.dao.LeaveTypeRepository;
import com.ogp.icms.leave.domain.LeaveType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class LeaveTypeServiceImpl {
    private final LeaveTypeRepository leaveTypeRepository;

    public LeaveType getLeaveType(Long id) {
        LeaveType t = leaveTypeRepository.findById(id).get();
        return t;
    }

    public List<LeaveType> getLeaveTypeList() {
        return leaveTypeRepository.findAll();
    }

    public ResultCode save(LeaveType leaveType) {
        leaveTypeRepository.save(leaveType);
        return new ResultCode(0, "휴가 종류를 생성했습니다.");
    }

    public ResultCode delete(Long id) {
        leaveTypeRepository.deleteById(id);
        return new ResultCode(0, "휴가 종류를 삭제했습니다.");
    }

    public ResultCode modify(Long id, LeaveType LeaveType) {
        LeaveType savedLeaveType = leaveTypeRepository.getById(id);

        savedLeaveType.setType(LeaveType.getType());
        savedLeaveType.setActive(LeaveType.getActive());
        savedLeaveType.setAnnual(LeaveType.getAnnual());
        savedLeaveType.setFullTime(LeaveType.getFullTime());
        savedLeaveType.setName(LeaveType.getName());

        return new ResultCode(0, "휴가 종류를 수정했습니다.");
    }

}
