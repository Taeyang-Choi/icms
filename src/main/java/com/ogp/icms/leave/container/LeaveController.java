package com.ogp.icms.leave.container;

import com.ogp.icms.global.util.ResultCode;
import com.ogp.icms.leave.domain.Leave;
import com.ogp.icms.leave.domain.LeaveType;
import com.ogp.icms.leave.request.LeaveSearchCondition;
import com.ogp.icms.leave.service.LeaveServiceImpl;
import com.ogp.icms.leave.service.LeaveTypeServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.Result;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LeaveController {
    private final LeaveTypeServiceImpl leaveTypeService;
    private final LeaveServiceImpl leaveService;

    // region 휴가 타입 설정
    @GetMapping("/api/leave/types")
    public List<LeaveType> GetLeaveTypeList() {
    return leaveTypeService.getLeaveTypeList();
}

    @GetMapping("/api/leaves/types/{id}")
    public LeaveType getLeaveType(@PathVariable Long id) {
        LeaveType t = leaveTypeService.getLeaveType(id);
        return t;
    }

    @PostMapping("/api/leave/types")
    public ResultCode addLeaveType(@ModelAttribute LeaveType leaveType) {
        return leaveTypeService.save(leaveType);
    }

    @DeleteMapping("/api/leave/types/{id}")
    public ResultCode addLeaveType(@PathVariable Long id) {
        return leaveTypeService.delete(id);
    }

    @PutMapping("/api/leaves/types/{id}")
    public ResultCode modifyLeaveType(@PathVariable Long id, @ModelAttribute LeaveType leaveType) {
        return leaveTypeService.modify(id, leaveType);
    }
// endregion

// region 휴가
    @GetMapping("/api/leaves")
    public Page<Leave> getLeavePage(@ModelAttribute LeaveSearchCondition searchCondition, Pageable pageable) {
        return leaveService.getLeavePage(searchCondition, pageable);
}
    @GetMapping("/api/leaves/list")
    public List<Leave> getLeaveList(@ModelAttribute LeaveSearchCondition searchCondition, Pageable pageable) {
        log.info("{}", searchCondition);
        return leaveService.getLeaveList(searchCondition, pageable);
    }

    @GetMapping("/api/leaves/today")
    public Page<Leave> getTodayLeaveList(Pageable pageable) {
        LeaveSearchCondition searchCondition = new LeaveSearchCondition();
        searchCondition.setSearchStartDate(LocalDate.now());
        searchCondition.setSearchEndDate(LocalDate.now());
        return leaveService.getLeavePage(searchCondition, pageable);
    }

    
    // 추가
    @PostMapping("/api/leaves")
    public ResultCode addNewLeave(@ModelAttribute Leave leave) {
        return leaveService.save(leave);
    }

    @DeleteMapping("/api/leaves/{id}")
    public ResultCode GetLeaveList(@PathVariable Long id) {
        return leaveService.deleteLeave(id);
    }


    @PutMapping("/api/leaves/{id}/change-worker")
    public ResultCode modifyAlternativeWorker(@PathVariable Long id, Long workerId, String workerName) {
        return leaveService.modifyAlternativeWorker(id, workerId, workerName);
    }

    @PutMapping("/api/leaves/{id}/status")
    public ResultCode modifyStatus(@PathVariable Long id, int status) {
        return leaveService.modifyStatus(id, status);
    }

// endregion
}
