package com.ogp.icms.schedule.dao;

import com.ogp.icms.schedule.domain.SchedulePattern;
import com.ogp.icms.schedule.domain.ScheduleShift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchedulePatternRepository extends JpaRepository<SchedulePattern, Long> {
}
