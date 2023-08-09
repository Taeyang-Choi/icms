package com.ogp.icms.gis.domain;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;


@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@DynamicUpdate
@Table(name="mt_usr_desc")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(name = "org_mgr_no")
    private String orgNo;

    @Column(name = "depart_nm")
    private String depart;
}
