package com.ogp.icms.leave.domain;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.ogp.icms.global.entity.DateEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Type;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@Slf4j
@Entity
@ToString
@Table(name = "leave_type")
public class LeaveType extends DateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String type;

    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column(name="fulltime")
    private Boolean fullTime;

    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean annual;

    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean active;

}
