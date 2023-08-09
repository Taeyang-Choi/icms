package com.ogp.icms.gis.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@Table(name="crms_trans_rqst")
public class CrimeRequest {
    @Id
    @Column(name = "mgr_seq", nullable = false)
    private Long id;

    @Column(name = "reqst_dat")
    private String requestDate;

    @Column(name = "reqst_detail")
    private String requestDetail;

    @Column(name = "crime_typ")
    private String crimeType;

    @Column(name = "doc_no")
    private String docNo;

    @Column(name = "proc_stat_cd")
    private String processStatCode;

    @Column(name = "reqst_id")
    private String requesterId;

    @Column(name = "user_nm")
    private String requesterName;

    private String dept;

    private String orgName;
}
