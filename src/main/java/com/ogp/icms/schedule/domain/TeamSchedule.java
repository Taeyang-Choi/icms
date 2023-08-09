package com.ogp.icms.schedule.domain;

import com.ogp.icms.global.entity.DateEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
@Entity
@Table(name = "schedule_team")
@ToString
@IdClass(TeamScheduleId.class)
public class TeamSchedule extends DateEntity {
  @Id
  private String team;

  @Id
  private String date;

  private String shift;
}
