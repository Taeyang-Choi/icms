package com.ogp.icms.schedule.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class TeamScheduleId implements Serializable {
    private String team;
    private String date;

    public TeamScheduleId(String team, String date) {
        this.team = team;
        this.date = date;
    }
}
