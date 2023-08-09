package com.ogp.icms.member.domain;

import com.ogp.icms.global.entity.DateEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@DynamicUpdate
@Table(name="gs_superadmin")
public class Member extends DateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String userid;
  private String userpwd;
  private String dept;
  private String name;
  @ColumnDefault("NOTEAM")
  private String team;
  private String monitor;
  private String ipAddr;
  private String grade;
  private String hp;
}
