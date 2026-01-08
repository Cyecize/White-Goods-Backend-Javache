package com.cyecize.app.api.store.promotion;

import static com.cyecize.app.api.store.promotion.PromotionType.DISCOUNT_OVER_SUBTOTAL;
import static com.cyecize.app.api.store.promotion.PromotionType.DISCOUNT_OVER_TOTAL;

import com.cyecize.app.api.product.dto.ProductDto;
import com.cyecize.app.api.store.cart.ShoppingCartDetailedDto;
import com.cyecize.app.api.store.cart.ShoppingCartItemDetailedDto;
import com.cyecize.app.api.store.pricing.PriceBag;
import com.cyecize.app.api.store.promotion.dto.CreatePromotionDto;
import com.cyecize.app.api.store.promotion.dto.PromotionDto;
import com.cyecize.app.api.store.promotion.dto.PromotionQuery;
import com.cyecize.app.constants.EntityGraphs;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.app.util.MathUtil;
import com.cyecize.app.util.Page;
import com.cyecize.app.util.Specification;
import com.cyecize.app.util.SpecificationExecutor;
import com.cyecize.summer.common.annotations.PostConstruct;
import com.cyecize.summer.common.annotations.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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
                new ShoppingCartDetailedDto(
                        LocalDateTime.now(),
                        null,
                        List.of(new ShoppingCartItemDetailedDto(productDto, 1, prodPrice))
                ),
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
    public Double getFreeDeliveryThreshold(Double subtotal, Double total) {
        final var validTypes = List.of(DISCOUNT_OVER_SUBTOTAL, DISCOUNT_OVER_TOTAL);
        return CACHED_PROMOTIONS.stream()
                .filter(promo -> validTypes.contains(promo.getPromotionType()))
                .filter(promo -> promo.getDiscountType() == DiscountType.FREE_DELIVERY)
                .map(promo -> {
                    if (promo.getPromotionType() == DISCOUNT_OVER_SUBTOTAL) {
                        return MathUtil.round(Math.max(0, promo.getMinSubtotal() - subtotal));
                    }

                    return MathUtil.round(Math.max(0, promo.getMinSubtotal() - total));
                })
                .min(Double::compare)
                .orElse(null);
    }

    @Override
    @Transactional
    public PromotionDto createPromotion(CreatePromotionDto dto) {
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

        return this.modelMapper.map(promotion, PromotionDto.class);
    }

    @Override
    public void reloadCachedPromotions() {
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
        final Specification<Promotion> specification =
                PromotionSpecifications.promotionTypeIn(query.getPromotionTypes())
                        .and(PromotionSpecifications.sort(query.getSort()))
                        .and(PromotionSpecifications.nameContains(query.getName()));

        return this.specificationExecutor.findAll(
                specification, query.getPage(), Promotion.class,
                EntityGraphs.PROMOTION_WITH_ITEMS
        );
    }

    @Override
    public void deletePromotion(Promotion promotion) {
        this.promotionRepository.delete(promotion);
        this.reloadCachedPromotions();
    }

    @Override
    public Promotion findPromoById(Long id) {
        final Specification<Promotion> specification = PromotionSpecifications.idEquals(id);

        return this.specificationExecutor.findOne(
                specification,
                Promotion.class,
                EntityGraphs.PROMOTION_WITH_ITEMS
        );
    }

    @Override
    public boolean existsById(Long id) {
        return this.promotionRepository.existsById(id);
    }

    @Override
    @Transactional
    public PromotionDto editPromotion(Long promoId, CreatePromotionDto dto) {
        this.promotionValidator.validatePromoBusinessRules(dto);

        final Promotion promotion = this.findPromoById(promoId);
        Objects.requireNonNull(promotion);

        final Set<Long> oldProdItemIds = this.getProductItemIds(promotion);
        promotion.getProductItems().clear();

        this.modelMapper.map(dto, promotion);
        promotion.getProductItems().forEach(prodItem -> prodItem.setPromotionId(promotion.getId()));

        // Persist new product items
        promotion.getProductItems().stream()
                .filter(prodItem -> !oldProdItemIds.contains(prodItem.getProductId()))
                .forEach(this.promotionProductItemRepository::persist);

        // Merge existing items
        promotion.getProductItems().stream()
                .filter(prodItem -> oldProdItemIds.contains(prodItem.getProductId()))
                .forEach(this.promotionProductItemRepository::merge);

        // Remove old product items
        final Set<Long> newProdItemIds = this.getProductItemIds(promotion);
        oldProdItemIds.removeAll(newProdItemIds);
        if (!oldProdItemIds.isEmpty()) {
            this.promotionProductItemRepository.removeAll(oldProdItemIds);
        }

        this.promotionRepository.merge(promotion);
        this.reloadCachedPromotions();
        return this.modelMapper.map(promotion, PromotionDto.class);
    }

    private Set<Long> getProductItemIds(Promotion promotion) {
        return promotion.getProductItems().stream()
                .map(PromotionProductItem::getProductId)
                .collect(Collectors.toSet());
    }
}
