package com.ogp.icms.member.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/attend")
public class AttendController {

    @GetMapping("/member/list")
    public String memberList() {
        return "attend/member/list";
    }

    @GetMapping("/member/detail")
    public String memberDetail() {
        return "attend/member/detail";
    }

    @GetMapping("/statistics")
    public String statistics() {
        return "attend/statistics";
    }

    @GetMapping("/time-sheet")
    public String timeSheet() {
        return "attend/time-sheet";
    }


    @GetMapping("/leave/list")
    public String leaveList() {
        return "attend/leave/list";
    }

    @GetMapping("/leave/detail")
    public String leaveDetail() {
        return "attend/leave/detail";
    }

    @GetMapping("/leave/edit")
    public String leaveEdit() {
        return "attend/leave/edit";
    }

    @GetMapping("/leave/write")
    public String leaveWrite() {
        return "attend/leave/write";
    }

    @GetMapping("/leave/type/list")
    public String leaveTypeList() {
        return "attend/leave/type/list";
    }

    @GetMapping("/leave/type/write")
    public String leaveTypeWrite() {
        return "attend/leave/type/write";
    }

    @GetMapping("/leave/type/edit")
    public String leaveTypeEdit() {
        return "attend/leave/type/edit";
    }


}
