package com.ogp.icms.cctv.domain;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ogp.icms.global.entity.DateEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@Table(name="gs_cctv_error")
public class CameraError extends DateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Long cctvId;
    private String cctvGubun;
    private String cctvIndex;
    private String juso;
    private String ymd;
    private String model;
    private String data;

    public void setData(String data) {
        this.data = data;
    }
    public void setData(ObjectNode objectNode) {
        this.data = objectNode.toString();
    }
}
