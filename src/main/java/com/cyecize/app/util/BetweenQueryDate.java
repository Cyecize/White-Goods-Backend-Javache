package com.cyecize.app.util;

import com.cyecize.app.converters.DateTimeConverter;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class BetweenQueryDate extends BetweenQuery<LocalDateTime> {

    @DateTimeConverter
    private LocalDateTime min;

    @DateTimeConverter
    private LocalDateTime max;

    private Boolean notBetween;

    public BetweenQueryDate(LocalDateTime min, LocalDateTime max) {
        this.setMin(min);
        this.setMax(max);
    }
}
