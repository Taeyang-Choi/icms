package com.ogp.icms.dailyreport.request;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class DailyReportConfirmRequest {
    private final Long id;
    private final String confirmUserid;
}
