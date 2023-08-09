package com.ogp.icms.schedule.dao;

import com.ogp.icms.schedule.domain.Schedule;
import com.ogp.icms.schedule.domain.TeamSchedule;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByDatecode(String datecode);

    List<Schedule> findByDatecodeAndUserwid(String datecode, String userwid);

    Optional<Schedule> findTop1ByUserwidOrderByIdDesc(String submitUserid);

    List<Schedule> findByDatecodeIsNull();

    Optional<Schedule> findByDatecodeAndUserwidAndDivision(String datecode, String userwid, String division);
}
