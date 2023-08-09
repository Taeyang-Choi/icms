package com.ogp.icms.dailyreport.request;

import com.ogp.icms.global.entity.SearchCondition;
import lombok.Data;

@Data
public class DailyReportSearchCondition implements SearchCondition {
    private String workDateFrom;
    private String workDateTo;
    private String username;
    private String userid;
    private int grade;
}
