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
@Table(name = "gs_kindcode")
public class KindCode extends DateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String code;
    String name;
    String codeFormat;
    int seq;

    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean active;

    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean essential;
}
