package com.cyecize.app.api.store.promotion;

import com.cyecize.app.api.product.dto.ProductDto;
import com.cyecize.app.api.store.cart.ShoppingCartItemDetailedDto;
import com.cyecize.app.api.store.pricing.PriceBag;
import com.cyecize.app.util.MathUtil;
import com.cyecize.summer.common.annotations.PostConstruct;
import com.cyecize.summer.common.annotations.Service;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private static final List<Promotion> CACHED_PROMOTIONS = new ArrayList<>();
    private static final Map<PromotionStage, List<Promotion>> CACHED_PROMOS_BY_TYPE = new HashMap<>();

    private final PromotionRepository promotionRepository;

    private final PromotionProductItemRepository promotionProductItemRepository;

    @PostConstruct
    public void init() {
        this.reloadCachedPromotions();
    }

    @Override
    public void calculateDiscounts(PriceBag priceBag, PromotionStage stage) {
        final List<Promotion> applicablePromotions = CACHED_PROMOS_BY_TYPE
                .get(stage)
                .stream()
                .filter(promotion -> promotion.getPromotionType().test(promotion, priceBag))
                .sorted(Comparator.comparingInt(o -> o.getDiscountType().getExecutionOrder()))
                .collect(Collectors.toList());

        for (Promotion promotion : applicablePromotions) {
            promotion.getDiscountType().applyDiscount(promotion, priceBag);
        }
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

        final Double prodPrice = MathUtil.calculatePrice(productDto.getPrice(), 1);
        final PriceBag priceBag = new PriceBag(
                List.of(new ShoppingCartItemDetailedDto(productDto, 1, prodPrice)),
                prodPrice
        );

        for (Promotion promotion : applicablePromotions) {
            promotion.getDiscountType().applyDiscount(promotion, priceBag);
        }

        final Double totalDiscounts = priceBag.sumAllDiscounts();

        if (totalDiscounts <= 0) {
            return;
        }

        productDto.setDiscountedPrice(Math.max(
                0D,
                MathUtil.subtract(productDto.getPrice(), totalDiscounts)
        ));
    }

    private void reloadCachedPromotions() {
        final List<Promotion> promotions = this.promotionRepository.findAllFetchItems();
        CACHED_PROMOTIONS.clear();
        CACHED_PROMOTIONS.addAll(promotions);

        for (PromotionStage applicationType : PromotionStage.values()) {
            CACHED_PROMOS_BY_TYPE.put(
                    applicationType,
                    promotions.stream()
                            .filter(p -> p.getPromotionType().getStage()
                                    .equals(applicationType))
                            .collect(Collectors.toList())
            );
        }
    }
}
