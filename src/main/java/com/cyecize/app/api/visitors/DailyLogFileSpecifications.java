package com.cyecize.app.api.visitors;

import com.cyecize.app.util.BetweenQuery;
import com.cyecize.app.util.QuerySpecifications;
import com.cyecize.app.util.Specification;
import java.time.LocalDateTime;

public class DailyLogFileSpecifications {

    public static Specification<DailyLogFile> betweenDate(LocalDateTime start, LocalDateTime end) {
        return QuerySpecifications.between(
                DailyLogFile_.dateProcessed,
                new BetweenQuery<>(start, end)
        );
    }
}
