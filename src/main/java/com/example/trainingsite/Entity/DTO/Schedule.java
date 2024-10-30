package com.example.trainingsite.Entity.DTO;

import lombok.Data;

@Data
public class Schedule {
    private final int days_per_week;
    private final int session_duration;

    public Schedule(int days_per_week, int session_duration) {
        this.days_per_week = days_per_week;
        this.session_duration = session_duration;
    }
}
