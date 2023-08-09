package com.ogp.icms.dailyreport.request;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@Data
public class DailyReportSubmitRequest {
    private final Long id;
    private final String submitUserid;
}
