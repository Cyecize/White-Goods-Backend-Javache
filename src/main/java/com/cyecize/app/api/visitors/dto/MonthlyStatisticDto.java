package com.cyecize.app.api.visitors.dto;

import com.cyecize.app.api.visitors.dailyvisitordata.VisitorHitsPerIpDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlyStatisticDto {

    private final Integer year;

    private final Integer month;

    private final List<DailyStatisticDto> dailyStatistics;

    @JsonProperty("uniqueVisitors")
    public Integer getUniqueVisitors() {
        return this.dailyStatistics.stream()
                .flatMap(ds -> ds.getHitsPerIp().stream())
                .map(VisitorHitsPerIpDto::getIp).collect(
                        Collectors.toSet()).size();
    }

    @JsonProperty("totalVisitors")
    public Integer getTotalVisitors() {
        return this.dailyStatistics.stream()
                .map(DailyStatisticDto::getTotalVisitors)
                .reduce(Integer::sum).orElse(0);
    }
}
