package com.ogp.icms.asset.domain;

import com.ogp.icms.global.entity.DateEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Columns;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "gs_asset2")
@Data
public class Asset2 {

    private String assetId;
    private String purpose;
    private String dept;

    private String type;

    private String projectName;

    private String projectType;

    private String vmsId;

    @Id
    private String name;

    private String emd;

    private String li;

    private String town;

    private String address;

    private String refId;

    private String lat;

    private String lng;

    private String direction;

    private String jsonobj;
}
