package com.ogp.icms.dailyreport.request;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@Data
public class MonitoringActionRequest {
    private String actionCode;
    private String actionResult;
    private LocalDateTime actionRegdate;
    private String actionUserid;
    private String barrierLevel;
}
