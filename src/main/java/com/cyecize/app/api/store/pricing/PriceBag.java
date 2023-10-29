package com.cyecize.app.api.store.pricing;

import com.cyecize.app.api.store.cart.ShoppingCartItemDetailedDto;
import com.cyecize.app.api.store.promotion.dto.DiscountDto;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class PriceBag {

    @Getter
    private final List<ShoppingCartItemDetailedDto> items;

    @Getter
    private final Double subtotal;
    private final List<DiscountDto> discounts = new ArrayList<>();
    private final List<DiscountDto> additionalDiscounts = new ArrayList<>();

    @Getter
    @Setter
    private boolean freeDelivery;

    @Getter
    @Setter
    private Double total;

    public void addDiscount(DiscountDto discount) {
        if (discount.isFreeDelivery()) {
            // No need to add to discounts list since we have a flag for free delivery.
            this.freeDelivery = true;
        } else {
            // If the discount is less than or 0, then it is not really a discount
            if (discount.getValue() > 0) {
                this.getDiscountsListToInsert().add(discount);
            }
        }
    }

    public List<DiscountDto> getAllDiscounts() {
        return new ArrayList<>() {{
            addAll(discounts);
            addAll(additionalDiscounts);
        }};
    }

    public Double sumAllDiscounts() {
        return this.getAllDiscounts().stream()
                .map(DiscountDto::getValue)
                .reduce(Double::sum)
                .orElse(0D);
    }

    private List<DiscountDto> getDiscountsListToInsert() {
        if (this.total != null) {
            //Total already calculated, use additional discounts.
            return this.additionalDiscounts;
        }

        return this.discounts;
    }
}
