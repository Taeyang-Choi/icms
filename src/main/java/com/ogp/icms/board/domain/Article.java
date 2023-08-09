package com.ogp.icms.board.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.ogp.icms.global.entity.DateEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name="gs_board_notice")
public class Article extends DateEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String category;
  private String userid;
  private String username;
  private String email;
  private String home;
  private String title;
  private String body;
  private long count;
  private long thread;
  private long depth;
  private long pos;
  private String passwd;
  private String userFile;
  private String userIp;
  private String delflag;
  private String isSecret;
  private String tel;


  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime noticeEdate;

  private String noticeYn;

  private String site;
  private long recommend;
  private String smsflag;
}
