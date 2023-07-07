package com.cyecize.app.api.visitors;

import com.cyecize.app.api.visitors.dto.MonthlyStatisticDto;

public interface DailyLogFileService {

    void processDailyFiles();

    MonthlyStatisticDto getVisitorStatistics(Integer year, Integer month);
}
