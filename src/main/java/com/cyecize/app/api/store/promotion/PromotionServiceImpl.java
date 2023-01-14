package com.cyecize.app.api.store.promotion;

import com.cyecize.app.api.product.dto.ProductDto;
import com.cyecize.app.api.store.cart.ShoppingCartItemDetailedDto;
import com.cyecize.app.api.store.promotion.discounters.DiscounterPayloadDto;
import com.cyecize.app.api.store.promotion.dto.DiscountDto;
import com.cyecize.app.api.store.promotion.dto.DiscountsDto;
import com.cyecize.app.api.store.promotion.promotionfilters.FilterPayloadDto;
import com.cyecize.app.util.MathUtil;
import com.cyecize.summer.common.annotations.PostConstruct;
import com.cyecize.summer.common.annotations.Service;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private static final List<Promotion> CACHED_PROMOTIONS = new ArrayList<>();

    private final PromotionRepository promotionRepository;

    private final PromotionProductItemRepository promotionProductItemRepository;

    @PostConstruct
    public void init() {
        this.reloadCachedPromotions();
    }

    @Override
    public DiscountsDto calculateDiscounts(
            List<ShoppingCartItemDetailedDto> items,
            Double subtotal) {
        final FilterPayloadDto payload = new FilterPayloadDto(items, subtotal);
        final List<Promotion> applicablePromotions = CACHED_PROMOTIONS.stream()
                .filter(promotion -> promotion.getPromotionType().test(promotion, payload))
                .sorted(Comparator.comparingInt(o -> o.getDiscountType().getExecutionOrder()))
                .collect(Collectors.toList());

        final List<DiscountDto> discounts = new ArrayList<>();
        boolean freeDelivery = false;
        for (Promotion promotion : applicablePromotions) {
            final DiscountDto discount = promotion
                    .getDiscountType()
                    .applyDiscount(promotion, new DiscounterPayloadDto(items));

            if (discount.isFreeDelivery()) {
                freeDelivery = true;
                continue;
            }

            discounts.add(discount);
        }

        return new DiscountsDto(
                freeDelivery,
                MathUtil.sumAllAndRound(
                        discounts.stream().map(DiscountDto::getValue).toArray(Double[]::new)
                ),
                discounts
        );
    }

    @Override
    public void applySingleProductDiscount(ProductDto productDto) {
        if (productDto.getPrice() == null) {
            return;
        }

        final List<Promotion> applicablePromotions = CACHED_PROMOTIONS.stream()
                .filter(p -> p.getPromotionType() == PromotionType.DISCOUNT_SPECIFIC_PRODUCTS_ALL)
                .filter(p -> p.getProductItems().size() == 1)
                .filter(p -> p.getProductItems().get(0).getProductId().equals(productDto.getId()))
                .filter(p -> p.getProductItems().get(0).getMinQuantity() <= 1)
                .collect(Collectors.toList());

        if (applicablePromotions.isEmpty()) {
            return;
        }

        final DiscounterPayloadDto payload = new DiscounterPayloadDto(List.of(
                new ShoppingCartItemDetailedDto(
                        productDto,
                        1,
                        MathUtil.calculatePrice(productDto.getPrice(), 1)
                )
        ));

        final List<Double> totalDiscounts = new ArrayList<>();
        for (Promotion promotion : applicablePromotions) {
            final DiscountDto discount = promotion.getDiscountType()
                    .applyDiscount(promotion, payload);

            if (discount.getValue() > 0) {
                totalDiscounts.add(discount.getValue());
            }
        }

        if (totalDiscounts.isEmpty()) {
            return;
        }

        final Double discountedPrice = MathUtil.subtract(
                productDto.getPrice(),
                MathUtil.sumAllAndRound(totalDiscounts.toArray(Double[]::new))
        );
        productDto.setDiscountedPrice(discountedPrice);
    }

    private void reloadCachedPromotions() {
        final List<Promotion> promotions = this.promotionRepository.findAllFetchItems();
        CACHED_PROMOTIONS.clear();
        CACHED_PROMOTIONS.addAll(promotions);
    }
}
