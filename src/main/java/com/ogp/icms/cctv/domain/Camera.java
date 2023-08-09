package com.ogp.icms.cctv.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@Table(name="gs_cctv")
public class Camera {

  @Id
  private Long id;
  private String cctvGubun;
  private String dept;
  private String juso;
  private String cctvIndex;
  private String location;
  private String cameraCategory;
  private String movement;
  private String nightvision;
  private String shage;
  private String installymd;
  private String manufacturer;
  private String model;
  private String pixel;
  private String connectCnt;
  private String cameraCnt;
  private String integrationCnt;
  private String connectType;
  private String connectIp;
  private String connectPort;
  private String connectId;
  private String connectPw;
  private String connectModel;
  private String connectServerType;
  private String smCompany;
  private String smPerson;
  private String smTel;
  private String ptzUseyn;
  private String presetUseyn;
  private String fallCamera;
  private String fallDefinition;
  private String fallEquipment;
  private String fallNetwork;
  private String annox;
  private String annoy;
}
