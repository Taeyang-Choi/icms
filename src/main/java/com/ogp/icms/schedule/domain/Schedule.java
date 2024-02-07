package com.ogp.icms.schedule.domain;

import com.ogp.icms.global.entity.DateEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
@Entity
@ToString
@Table(name = "gs_schedule")
public class Schedule extends DateEntity
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String division;
    private String userwid;
    private String userwnm;
    private String grade;
    private String datecode;
    private String workform;
    private String team;

    @Column(name="sdate", columnDefinition = "DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime sDate;
    @Column(name="edate", columnDefinition = "DATE")
    private LocalDateTime eDate;
}
