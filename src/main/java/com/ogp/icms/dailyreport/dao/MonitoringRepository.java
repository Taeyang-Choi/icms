package com.ogp.icms.dailyreport.dao;

import com.ogp.icms.dailyreport.domain.DailyReport;
import com.ogp.icms.dailyreport.domain.Monitoring;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonitoringRepository extends JpaRepository<Monitoring, Long> {

    List<Monitoring> findByCctvId(Long id);

    List<Monitoring> findByCctvIdAndWorkDgubun(Long cctvId, String d01);

    List<Monitoring> findByWorkDate(String date);

    List<Monitoring> getByDailyreportId(long dailyreportId);

    List<Monitoring> getByCctvId(Long id);

    List<Monitoring> findByCctvIdIsNull();
}
