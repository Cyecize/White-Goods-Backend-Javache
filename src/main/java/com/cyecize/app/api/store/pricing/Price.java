package com.cyecize.app.api.store.pricing;

import com.cyecize.app.api.store.promotion.dto.DiscountDto;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Price {
    private final Double subtotal;
    private final Double deliveryPrice;
    private final Double total;
    private final boolean freeDelivery;
    private final Double totalDiscounts;
    private final List<DiscountDto> discounts;
}
