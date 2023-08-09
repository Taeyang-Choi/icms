package com.ogp.icms.dailyreport.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ogp.icms.global.entity.DateEntity;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Slf4j
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Table(name = "gs_dailyreport")
public class DailyReport extends DateEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String userid;
  private String username;
  private String userFile;
  private String userIp;
  private String summary;
  private Long status;
  private String workGubun;
  private String workMonitoring;
  private String workDateFrom;
  private String workDateTo;
  private String workTimeFrom;
  private String workTimeTo;
  private String team;
  private String submitUserid;
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime submitDate;
  private String confirmUserid;
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime confirmDate;

  public void setWorkDateFrom(String workDateFrom) {
    this.workDateFrom = workDateFrom.replace("-", "");
  }

  public void setWorkDateTo(String workDateTo) {
    this.workDateTo = workDateTo.replace("-", "");
  }
}
