package com.cyecize.app.api.store.promotion.coupon;

import com.cyecize.app.api.store.promotion.validators.ValidPromotionId;
import com.cyecize.app.constants.General;
import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.app.constraints.MinNullable;
import com.cyecize.app.converters.DateTimeConverter;
import com.cyecize.summer.areas.validation.constraints.Max;
import com.cyecize.summer.areas.validation.constraints.MaxLength;
import com.cyecize.summer.areas.validation.constraints.Min;
import com.cyecize.summer.areas.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class CreateCouponCodeDto {

    @Min(value = 1, message = ValidationMessages.INVALID_VALUE)
    // If this is changed to be > 99, update the @MaxLength annotation on "code" field.
    @Max(value = 99, message = ValidationMessages.INVALID_VALUE)
    private Integer numberOfCopies;

    @NotNull(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    @ValidPromotionId
    private Long promotionId;

    // Max is 50 - 2 for potentially appending up to 2 characters.
    @MaxLength(length = General.MAX_NAME - 2, message = ValidationMessages.INVALID_VALUE)
    private String code;

    @MinNullable(0)
    private Integer maxUsages;

    @DateTimeConverter
    private LocalDateTime expiryDate;

    public String getCode() {
        return StringUtils.trimToNull(this.code);
    }
}
