package com.ogp.icms.dailyreport.dao;

import com.ogp.icms.dailyreport.domain.DailyReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DailyReportRepository extends JpaRepository<DailyReport, Long> {
    List<DailyReport> findByWorkDateFrom(String date);

    DailyReport findByWorkGubunAndUseridAndWorkDateFrom(String workGubun, String userid, String workDateFrom);

    int countByStatus(long i);

    List<DailyReport> findBySummaryIsNull();
}
