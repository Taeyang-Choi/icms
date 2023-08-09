package com.ogp.icms.schedule.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.ogp.icms.global.entity.DateEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
@Entity
@Table(name = "schedule_shift")
public class ScheduleShift extends DateEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @JsonAlias("name")
  private String name;

  @JsonAlias("shift_code")
  private String shiftCode;

  @JsonAlias("work_start")
  private String workStart;

  @JsonAlias("work_end")
  private String workEnd;

  @JsonAlias("rest_start")
  private String restStart;

  @JsonAlias("rest_end")
  private String restEnd;

  private String color;

  @JsonAlias("start_date")
  private java.sql.Timestamp startDate;

  @JsonAlias("end_date")
  private java.sql.Timestamp endDate;
}
