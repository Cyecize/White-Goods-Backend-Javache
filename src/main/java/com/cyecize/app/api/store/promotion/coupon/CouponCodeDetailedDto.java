package com.cyecize.app.api.store.promotion.coupon;

import com.cyecize.app.converters.DateTimeConverter;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CouponCodeDetailedDto {

    private Long promotionId;

    private String code;

    private Integer maxUsages;

    private Integer currentUsages;

    @DateTimeConverter
    private LocalDateTime createDate;

    @DateTimeConverter
    private LocalDateTime expiryDate;

    private Boolean enabled;
}
