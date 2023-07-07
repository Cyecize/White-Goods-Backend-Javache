package com.cyecize.app.api.visitors.dailyvisitordata;

import lombok.Data;

@Data
public class VisitorHitsPerHourDto {

    private Integer hour;

    private Integer hits;
}
