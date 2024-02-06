package com.cyecize.app.api.store.cart;

import com.cyecize.app.converters.DateTimeConverter;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class ShoppingCartDetailedDto {

    @DateTimeConverter
    private final LocalDateTime lastModified;

    private final ShoppingCartCouponCodeDto couponCode;

    private final List<ShoppingCartItemDetailedDto> items;
}
