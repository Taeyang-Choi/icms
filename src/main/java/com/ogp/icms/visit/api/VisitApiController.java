package com.ogp.icms.visit.api;

import com.ogp.icms.global.util.ResultCode;
import com.ogp.icms.visit.domain.Visiting;
import com.ogp.icms.visit.domain.VisitingMonthlyCount;
import com.ogp.icms.visit.domain.VisitingSearchCondition;
import com.ogp.icms.visit.request.VisitorOutRequest;
import com.ogp.icms.visit.service.VisitServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Controller
@RequiredArgsConstructor
public class VisitApiController {
    private final VisitServiceImpl visitService;
    
    // 특정 방문자 로드
    @GetMapping("/api/visits/{id}")
    public Visiting getVisit(@PathVariable Long id) {
        return visitService.getVisit(id);
    }

    // 방문자 로드
    @GetMapping("/api/visits")
    public List<Visiting> getVisitPage() {
        return visitService.getVisitList();
    }

    // 방문자 로드
    @GetMapping("/api/visits/all")
    public List<Visiting> getVisitPage(Pageable pageable, VisitingSearchCondition searchCondition) {
        return visitService.getVisitList(pageable, searchCondition);
    }

    // 방문자 로드
    @GetMapping("/api/visits/excel")
    public List<Visiting> getVisitWhole(VisitingSearchCondition searchCondition) {
        return visitService.getVisitExcel(searchCondition);
    }

    // 방문자 등록
    @PostMapping("/api/visits")
    public ResultCode postVisitCode(@ModelAttribute Visiting visiting) {
        return visitService.addVisit(visiting);
    }

    @PutMapping("/api/visits/vout")
    public ResultCode visitorOut(@ModelAttribute VisitorOutRequest visitorOutRequest) {
        return visitService.visitorOut(visitorOutRequest);
    }
    
    // region 관리자 방문자 제어
    @PutMapping("/api/visits/{id}")
    public ResultCode editVisit(@PathVariable Long id, @ModelAttribute Visiting visiting) {
        return visitService.editVisitor(id, visiting);
    }
    // endregion 관리자 방문자 제어

    // region 통계

    @GetMapping("/api/visits/statistics")
    public List<VisitingMonthlyCount> statistics() {
        return visitService.statistics();
    }

    // endregion 통계

    // 방문자 직접 퇴실
    /*
    @PostMapping("/api/visits/vout")
    public String postVisitCode2(HttpServletRequest req, @RequestParam String data) {
        return visitService.visitOut(data).toString();
    }

    @DeleteMapping("/api/visits/vout")
    public String deleteVisitCode(HttpServletRequest req, @RequestParam String idx) {
        return visitService.visitDelete(idx).toString();
    }
    */
}
