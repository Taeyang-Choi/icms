package com.ogp.icms.visit.service;

import com.ogp.icms.global.util.ResultCode;
import com.ogp.icms.visit.dao.VisitRepository;
import com.ogp.icms.visit.dao.VisitJpaRepository;
import com.ogp.icms.visit.domain.Visiting;
import com.ogp.icms.visit.domain.VisitingMonthlyCount;
import com.ogp.icms.visit.domain.VisitingSearchCondition;
import com.ogp.icms.visit.request.VisitorOutRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
public class VisitServiceImpl {
    private final VisitJpaRepository visitJpaRepository;
    private final VisitRepository visitRepository;


    /* 방문 가져오기 */
    public Visiting getVisit(Long id) {
        return visitJpaRepository.findById(id).get();
    }

    /* 방문 기능 */
    public List<Visiting> getVisitList() {
        return visitJpaRepository.findByStatus("Y");
    }

    /* 방문 기능 페이지 */
    public List<Visiting> getVisitList(Pageable pageable, VisitingSearchCondition searchCondition) {
        int page = pageable.getPageNumber()-1;
        PageRequest newPageRequest = PageRequest.of((page <= 0) ? 0 : page, 10);
        return visitRepository.findByQuery(newPageRequest, searchCondition);
    }

    public List<Visiting> getVisitExcel(VisitingSearchCondition searchCondition) {
        PageRequest newPageRequest = PageRequest.of(0, 100000);
        return visitRepository.findByQuery(newPageRequest, searchCondition);
    }

    // 방문 등록
    public ResultCode addVisit(Visiting visiting) {
        visitJpaRepository.save(visiting);
        visiting.setStatus("Y");
        return new ResultCode(0, "방문 등록을 완료했습니다.");
    }

    /**
     * 입실 처리
     */
    public ResultCode submitVisitor(Long id) {
        Visiting savedVisiting = visitJpaRepository.getById(id);
        savedVisiting.setSubmitDate(LocalDateTime.now());
        return new ResultCode(0, "승인을 완료했습니다.");
    }
    
    /**
     * 퇴실 처리
     */
    public ResultCode visitorOut(VisitorOutRequest visitorOutRequest) {
        Visiting savedVisiting = visitJpaRepository.getById(visitorOutRequest.getId());
        savedVisiting.setStatus("N");
        savedVisiting.setOutDate(visitorOutRequest.getOutDate());
        visitRepository.clear();
        return new ResultCode(0, "퇴실 처리를 완료했습니다.");
    }

    public ResultCode editVisitor(Long id, Visiting visiting) {
        Visiting savedVisiting = visitJpaRepository.getById(visiting.getId());
        savedVisiting.setEntDate(visiting.getEntDate());
        savedVisiting.setOutDate(visiting.getOutDate());
        savedVisiting.setCompanyName(visiting.getCompanyName());
        savedVisiting.setUsername(visiting.getUsername());
        savedVisiting.setHpNo(visiting.getHpNo());
        savedVisiting.setSubmitUser(visiting.getSubmitUser());
        savedVisiting.setBody(visiting.getBody());
        visitRepository.clear();
        return new ResultCode(0, "수정을 완료했습니다.");
    }

    public List<VisitingMonthlyCount> statistics() {
        return visitJpaRepository.countAllByMonthly();
    }

    // 삭제처리
    public ResultCode deleteVisitor(Long id) {
        visitJpaRepository.deleteById(id);
        return new ResultCode(0, "삭제를 완료하였습니다.");
    }
}

