package com.cyecize.app.api.visitors.dto;

import com.cyecize.app.api.visitors.dailyvisitordata.VisitorHitsPerHourDto;
import com.cyecize.app.api.visitors.dailyvisitordata.VisitorHitsPerIpDto;
import com.cyecize.app.api.visitors.dailyvisitordata.VisitorHitsPerPageDto;
import com.cyecize.app.converters.DateTimeConverter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class DailyStatisticDto {

    private String filename;

    @DateTimeConverter
    private LocalDateTime dateProcessed;

    private Integer uniqueVisitors;

    private List<VisitorHitsPerHourDto> hitsPerHour;

    private List<VisitorHitsPerIpDto> hitsPerIp;

    private List<VisitorHitsPerPageDto> hitsPerPage;

    @JsonProperty("totalVisitors")
    public Integer getTotalVisitors() {
        if (this.hitsPerHour == null) {
            return 0;
        }

        return this.hitsPerHour.stream()
                .map(VisitorHitsPerHourDto::getHits)
                .reduce(Integer::sum).orElse(0);
    }
}
