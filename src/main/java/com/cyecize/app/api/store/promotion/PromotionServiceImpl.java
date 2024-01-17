package com.cyecize.app.api.store.promotion;

import com.cyecize.app.api.product.dto.ProductDto;
import com.cyecize.app.api.store.cart.ShoppingCartItemDetailedDto;
import com.cyecize.app.api.store.pricing.PriceBag;
import com.cyecize.app.api.store.promotion.dto.CreatePromotionDto;
import com.cyecize.app.api.store.promotion.dto.PromotionQuery;
import com.cyecize.app.constants.EntityGraphs;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.app.util.MathUtil;
import com.cyecize.app.util.Page;
import com.cyecize.app.util.Specification;
import com.cyecize.app.util.SpecificationExecutor;
import com.cyecize.summer.common.annotations.PostConstruct;
import com.cyecize.summer.common.annotations.Service;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private static final List<Promotion> CACHED_PROMOTIONS = new ArrayList<>();
    private static final Map<PromotionStage, List<Promotion>> CACHED_PROMOS_BY_TYPE = new HashMap<>();

    private final PromotionRepository promotionRepository;

    private final PromotionProductItemRepository promotionProductItemRepository;

    private final PromotionValidator promotionValidator;

    private final ModelMapper modelMapper;

    private final SpecificationExecutor specificationExecutor;

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

    @Override
    @Transactional
    public void createPromotion(CreatePromotionDto dto) {
        this.promotionValidator.validatePromoBusinessRules(dto);

        final Promotion promotion = this.modelMapper.map(dto, Promotion.class);

        this.promotionRepository.persist(promotion);

        if (promotion.getProductItems() != null && !promotion.getProductItems().isEmpty()) {
            for (PromotionProductItem productItem : promotion.getProductItems()) {
                productItem.setPromotionId(promotion.getId());
                this.promotionProductItemRepository.persist(productItem);
            }
        }

        this.reloadCachedPromotions();
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

    @Override
    public Page<Promotion> searchPromotions(PromotionQuery query) {
        final Specification<Promotion> specification = PromotionSpecifications
                .sort(query.getSort());

        return this.specificationExecutor.findAll(
                specification, query.getPage(), Promotion.class,
                EntityGraphs.PROMOTION_WITH_ITEMS
        );
    }
}
