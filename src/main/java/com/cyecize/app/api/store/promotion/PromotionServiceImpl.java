package com.cyecize.app.api.store.promotion;

import com.cyecize.app.api.store.cart.ShoppingCartItemDetailedDto;
import com.cyecize.summer.common.annotations.PostConstruct;
import com.cyecize.summer.common.annotations.Service;
import java.util.ArrayList;
import java.util.List;
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
    public List<Promotion> getPromotions(List<ShoppingCartItemDetailedDto> items) {
        return new ArrayList<>();
    }

    private void reloadCachedPromotions() {
        CACHED_PROMOTIONS.clear();
        CACHED_PROMOTIONS.addAll(this.promotionRepository.findAllFetchItems());
    }
}
