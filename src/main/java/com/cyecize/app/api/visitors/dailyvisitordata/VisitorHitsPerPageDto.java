package com.cyecize.app.api.visitors.dailyvisitordata;

import lombok.Data;

@Data
public class VisitorHitsPerPageDto {

    private String url;

    private Integer hits;
}
