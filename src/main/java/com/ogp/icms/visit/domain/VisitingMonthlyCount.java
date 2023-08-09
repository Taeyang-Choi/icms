package com.ogp.icms.visit.domain;


import lombok.Data;

@Data
public class VisitingMonthlyCount {
    private int year;

    private int month;

    private long count;

    public VisitingMonthlyCount(int year, int month, long count) {
        this.year = year;
        this.month = month;
        this.count = count;
    }
}
