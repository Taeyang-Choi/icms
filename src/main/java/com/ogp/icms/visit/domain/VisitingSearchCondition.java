package com.ogp.icms.visit.domain;

import com.ogp.icms.global.entity.SearchCondition;
import lombok.Data;

@Data
public class VisitingSearchCondition implements SearchCondition {
    private String company;
    private String name;
}
