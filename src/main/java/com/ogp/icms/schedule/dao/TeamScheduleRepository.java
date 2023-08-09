package com.ogp.icms.schedule.dao;

import com.ogp.icms.schedule.domain.TeamSchedule;
import com.ogp.icms.schedule.domain.TeamScheduleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamScheduleRepository extends JpaRepository<TeamSchedule, TeamScheduleId> {
    List<TeamSchedule> findByDateLike(String s);

    List<TeamSchedule> findByDateBetween(String s, String s1);

    List<TeamSchedule> findByDate(String date);

    TeamSchedule findByTeamAndDate(String team, String date);
}
