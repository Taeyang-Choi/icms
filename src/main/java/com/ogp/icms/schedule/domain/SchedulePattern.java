package com.ogp.icms.schedule.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.JsonNode;
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
@ToString
@Table(name = "schedule_pattern")
public class SchedulePattern extends DateEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String team;
  private String name;
  private String list;
  private int size;
  private String startDate;
  private String endDate;
}
