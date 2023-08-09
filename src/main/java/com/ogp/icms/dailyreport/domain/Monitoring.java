package com.ogp.icms.dailyreport.domain;

import com.ogp.icms.global.entity.DateEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
@Entity
@Table(name = "gs_dailyreport_detail")
@ToString
public class Monitoring extends DateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private long dailyreportId;
  private String userid;
  private String workDate;
  private String workDtimeFrom;
  private String workDtimeTo;
  private String workDgubun;
  private Long cctvId;
  private String cctvIndex;
  private String workContent;
  private String actionCode;
  private String actionResult;
  private String barrierLevel;
  private LocalDateTime actionRegdate;
  private String actionUserid;
}
