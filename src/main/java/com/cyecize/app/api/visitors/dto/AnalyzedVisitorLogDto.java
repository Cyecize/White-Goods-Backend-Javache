package com.cyecize.app.api.visitors.dto;

import java.util.Map;
import lombok.Data;

@Data
public class AnalyzedVisitorLogDto {

    private final Map<String, Integer> pageViews;
    private final Map<String, Integer> visitorFrequency;
    private final Map<Integer, Integer> hourlyVisits;
    private final int uniqueVisitors;
}
