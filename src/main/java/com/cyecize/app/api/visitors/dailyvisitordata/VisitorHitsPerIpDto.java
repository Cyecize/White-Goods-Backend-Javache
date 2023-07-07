package com.cyecize.app.api.visitors.dailyvisitordata;

import lombok.Data;

@Data
public class VisitorHitsPerIpDto {

    private String ip;

    private Integer hits;
}
