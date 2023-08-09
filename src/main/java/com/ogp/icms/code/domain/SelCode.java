package com.ogp.icms.code.domain;

import com.ogp.icms.global.entity.DateEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
@Entity
@Table(name = "gs_selcode")
public class SelCode extends DateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String kindCode;
    String code;
    String name;

    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean active;

    int seq;
    String remarks;


}
