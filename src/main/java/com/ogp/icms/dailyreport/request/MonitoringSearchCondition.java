package com.ogp.icms.dailyreport.request;

import com.ogp.icms.global.entity.SearchCondition;
import lombok.Data;

import java.util.Date;

@Data
public class MonitoringSearchCondition implements SearchCondition {
    private String barrierLevel;
    private String workDgubun;
    private String startDate;
    private String endDate;
    private String situ;
    private String actionCode;

    private String cctvIndex;

    private String userid;
}
