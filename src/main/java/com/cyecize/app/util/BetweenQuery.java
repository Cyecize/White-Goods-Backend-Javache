package com.cyecize.app.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BetweenQuery<T extends Comparable<? super T>> {

    private T min;

    private T max;

    private Boolean notBetween;

    public BetweenQuery(T min, T max) {
        this.setMin(min);
        this.setMax(max);
    }
}
