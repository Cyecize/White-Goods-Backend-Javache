package com.cyecize.app.api.store.promotion;

import static com.cyecize.app.api.store.promotion.DiscountType.FIXED_AMOUNT_PER_PRODUCT;
import static com.cyecize.app.api.store.promotion.DiscountType.FREE_DELIVERY;
import static com.cyecize.app.api.store.promotion.DiscountType.PERCENT_PER_PRODUCT;
import static com.cyecize.app.api.store.promotion.DiscountType.SUBTOTAL_FIXED_AMOUNT;
import static com.cyecize.app.api.store.promotion.PromotionStage.ADDITIONAL;
import static com.cyecize.app.api.store.promotion.PromotionStage.REGULAR;
import static com.cyecize.app.api.store.promotion.PromotionType.DISCOUNT_OVER_SUBTOTAL;
import static com.cyecize.app.api.store.promotion.PromotionType.DISCOUNT_OVER_TOTAL;
import static com.cyecize.app.api.store.promotion.PromotionType.DISCOUNT_SPECIFIC_CATEGORY;
import static com.cyecize.app.api.store.promotion.PromotionType.DISCOUNT_SPECIFIC_PRODUCTS_ALL;
import static com.cyecize.app.api.store.promotion.PromotionType.DISCOUNT_SPECIFIC_PRODUCTS_ANY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.cyecize.app.api.product.dto.ProductDto;
import com.cyecize.app.api.store.cart.ShoppingCartDetailedDto;
import com.cyecize.app.api.store.cart.ShoppingCartItemDetailedDto;
import com.cyecize.app.api.store.pricing.PriceBag;
import com.cyecize.app.util.MathUtil;
import com.cyecize.app.util.SpecificationExecutor;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
public class PromotionServiceTests {

    private PromotionServiceImpl promotionService;

    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private PromotionProductItemRepository promotionProductItemRepository;

    @Mock
    private PromotionValidator promotionValidator;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private SpecificationExecutor specificationExecutor;

    @BeforeEach
    public void init() {
        this.promotionService = new PromotionServiceImpl(
                this.promotionRepository,
                this.promotionProductItemRepository,
                this.promotionValidator,
                this.modelMapper,
                this.specificationExecutor
        );
    }

    @Test
    public void testPromoOverSubtotal_fixedSubtotalDiscount_meetsSubtotal() {
        PriceBag priceBag = createPriceBag(createItem(1L, 100D, 1));

        Promotion promotion = this.buildPromoSubtotal_subtotal(
                "test", 25D, 90D
        );

        when(promotionRepository.findAllFetchItems()).thenReturn(List.of(promotion));

        this.promotionService.init();
        this.promotionService.calculateDiscounts(priceBag, REGULAR);

        assertEquals(25D, priceBag.sumAllDiscounts());
    }

    @Test
    public void testPromoOverSubtotal_fixedSubtotalDiscount_dontMeetSubtotal() {
        PriceBag priceBag = createPriceBag(createItem(1L, 100D, 1));

        Promotion promotion = this.buildPromoSubtotal_subtotal(
                "test", 25D, 90000D
        );

        when(promotionRepository.findAllFetchItems()).thenReturn(List.of(promotion));

        this.promotionService.init();
        this.promotionService.calculateDiscounts(priceBag, REGULAR);

        assertEquals(0D, priceBag.sumAllDiscounts());
    }

    @Test
    public void testPromoOverSubtotal_fixedSubtotalDiscount_multiplePromosShouldAddUp() {
        PriceBag priceBag = createPriceBag(createItem(1L, 100D, 1));

        Promotion promotion = this.buildPromoSubtotal_subtotal(
                "test", 25D, 99D
        );

        Promotion promotion2 = this.buildPromoSubtotal_subtotal(
                "test2", 15D, 99D
        );

        when(promotionRepository.findAllFetchItems()).thenReturn(List.of(promotion, promotion2));

        this.promotionService.init();
        this.promotionService.calculateDiscounts(priceBag, REGULAR);

        assertEquals(40D, priceBag.sumAllDiscounts());
    }

    @Test
    public void testPromoOverTotal_fixedSubtotalDiscount_stepIsNotAdditional() {
        PriceBag priceBag = createPriceBag(createItem(1L, 100D, 1));

        Promotion promotion = this.buildPromoTotal_subtotal(
                "test", 10D, 90D
        );

        when(promotionRepository.findAllFetchItems()).thenReturn(List.of(promotion));

        this.promotionService.init();
        this.promotionService.calculateDiscounts(priceBag, REGULAR);

        // nothing happens because discount over total is an additional type if discount
        assertEquals(0D, priceBag.sumAllDiscounts());
    }

    @Test
    public void testPromoOverTotal_fixedSubtotalDiscount_noTtalPresent() {
        PriceBag priceBag = createPriceBag(createItem(1L, 100D, 1));

        Promotion promotion = this.buildPromoTotal_subtotal(
                "test", 10D, 90D
        );

        when(promotionRepository.findAllFetchItems()).thenReturn(List.of(promotion));

        this.promotionService.init();

        // Total is required in order to test and apply discount over total
        assertThrows(
                IllegalStateException.class,
                () -> this.promotionService.calculateDiscounts(priceBag, ADDITIONAL)
        );
    }

    @Test
    public void testPromoOverTotal_fixedSubtotalDiscount_minTotalTests() {
        PriceBag priceBag = createPriceBag(createItem(1L, 100D, 1));
        priceBag.setTotal(100D);

        Promotion promotion = this.buildPromoTotal_subtotal(
                "test", 10D, 99D
        );

        when(promotionRepository.findAllFetchItems()).thenReturn(List.of(promotion));

        this.promotionService.init();
        this.promotionService.calculateDiscounts(priceBag, ADDITIONAL);

        // when min total is met
        assertEquals(10D, priceBag.sumAllDiscounts());

        promotion.setMinSubtotal(120D);
        priceBag = createPriceBag(createItem(1L, 100D, 1));
        priceBag.setTotal(100D);
        this.promotionService.calculateDiscounts(priceBag, ADDITIONAL);

        // When min total is not met
        assertEquals(0D, priceBag.sumAllDiscounts());
    }

    @Test
    public void testPromoOverTotal_fixedSubtotalDiscount_accompanyingDiscounts() {
        ShoppingCartItemDetailedDto item = createItem(1L, 100D, 1);
        PriceBag priceBag = createPriceBag(item);

        Promotion totalPromo = this.buildPromoTotal_subtotal(
                "total1", 10D, 99D
        );

        Promotion subtotalPromo = this.buildPromoSubtotal_subtotal(
                "subtotal1", 20D, 99D
        );

        when(promotionRepository.findAllFetchItems()).thenReturn(
                List.of(totalPromo, subtotalPromo));

        this.promotionService.init();
        this.promotionService.calculateDiscounts(priceBag, REGULAR);

        assertEquals(20D, priceBag.sumAllDiscounts());
        priceBag.setTotal(item.getCalculatedPrice() - priceBag.sumAllDiscounts());

        this.promotionService.calculateDiscounts(priceBag, ADDITIONAL);

        // the total discount did not appear because the total went below the minimum required
        assertEquals(20D, priceBag.sumAllDiscounts());

        priceBag = createPriceBag(item);
        totalPromo.setMinSubtotal(50D);
        this.promotionService.calculateDiscounts(priceBag, REGULAR);
        assertEquals(20D, priceBag.sumAllDiscounts());
        priceBag.setTotal(item.getCalculatedPrice() - priceBag.sumAllDiscounts());

        this.promotionService.calculateDiscounts(priceBag, ADDITIONAL);

        // This time the min total is less, so it will pass
        assertEquals(30D, priceBag.sumAllDiscounts());
    }

    @Test
    public void testSpecificProductAll_fixedSubtotalDiscount_singleProduct() {
        ShoppingCartItemDetailedDto item = createItem(1L, 100D, 1);
        PriceBag priceBag = createPriceBag(item);

        Promotion promo = this.buildPromoSpecificProductsAll(
                "test", 17D, SUBTOTAL_FIXED_AMOUNT, List.of(buildPromoProdItem(1L, 1))
        );

        when(promotionRepository.findAllFetchItems()).thenReturn(List.of(promo));

        this.promotionService.init();
        this.promotionService.calculateDiscounts(priceBag, REGULAR);

        assertEquals(17D, priceBag.sumAllDiscounts());
    }

    @Test
    public void testSpecificProductAll_fixedSubtotalDiscount_noProdIdMatch() {
        ShoppingCartItemDetailedDto item = createItem(999L, 100D, 1);
        PriceBag priceBag = createPriceBag(item);

        Promotion promo = this.buildPromoSpecificProductsAll(
                "test", 17D, SUBTOTAL_FIXED_AMOUNT, List.of(buildPromoProdItem(11L, 1))
        );

        when(promotionRepository.findAllFetchItems()).thenReturn(List.of(promo));

        this.promotionService.init();
        this.promotionService.calculateDiscounts(priceBag, REGULAR);

        assertEquals(0D, priceBag.sumAllDiscounts());
    }

    @Test
    public void testSpecificProductAll_fixedSubtotalDiscount_multiProdCart() {
        ShoppingCartItemDetailedDto item = createItem(1L, 100D, 1);
        ShoppingCartItemDetailedDto item2 = createItem(2L, 120D, 1);
        ShoppingCartItemDetailedDto item3 = createItem(3L, 130D, 1);
        ShoppingCartItemDetailedDto item4 = createItem(4L, 140D, 1);
        PriceBag priceBag = createPriceBag(List.of(item, item2, item3, item4));

        Promotion promo = this.buildPromoSpecificProductsAll(
                "test",
                10D,
                SUBTOTAL_FIXED_AMOUNT,
                List.of(
                        buildPromoProdItem(1L, 1),
                        buildPromoProdItem(3L, 1),
                        buildPromoProdItem(4L, 1)
                )
        );

        when(promotionRepository.findAllFetchItems()).thenReturn(List.of(promo));

        this.promotionService.init();
        this.promotionService.calculateDiscounts(priceBag, REGULAR);

        // All of the products required are in the cart, so discount is executed!
        assertEquals(10D, priceBag.sumAllDiscounts());
        assertEquals(1, priceBag.getAllDiscounts().size());
    }

    @Test
    public void testSpecificProductAll_fixedPerProd_multiProdCart() {
        ShoppingCartItemDetailedDto item = createItem(1L, 100D, 1);
        ShoppingCartItemDetailedDto item2 = createItem(2L, 120D, 1);
        ShoppingCartItemDetailedDto item3 = createItem(3L, 130D, 1);
        ShoppingCartItemDetailedDto item4 = createItem(4L, 140D, 1);
        PriceBag priceBag = createPriceBag(List.of(item, item2, item3, item4));

        Promotion promo = this.buildPromoSpecificProductsAll(
                "test",
                10D,
                FIXED_AMOUNT_PER_PRODUCT,
                List.of(
                        buildPromoProdItem(1L, 1),
                        buildPromoProdItem(3L, 1),
                        buildPromoProdItem(4L, 1)
                )
        );

        when(promotionRepository.findAllFetchItems()).thenReturn(List.of(promo));

        this.promotionService.init();
        this.promotionService.calculateDiscounts(priceBag, REGULAR);

        // All 3 products from the promo get their own 10 bux discount, totalling to 30
        assertEquals(30D, priceBag.sumAllDiscounts());
        assertEquals(1, priceBag.getAllDiscounts().size());
        assertEquals(3, priceBag.getAllDiscounts().get(0).getProductItems().size());
    }

    @Test
    public void testSpecificProductAll_fixedSubtotalDiscount_multiProdCart_missingRequiredProd() {
        ShoppingCartItemDetailedDto item = createItem(1L, 100D, 1);
        ShoppingCartItemDetailedDto item2 = createItem(2L, 120D, 1);
        ShoppingCartItemDetailedDto item3 = createItem(3L, 130D, 1);
        ShoppingCartItemDetailedDto item4 = createItem(4L, 140D, 1);
        PriceBag priceBag = createPriceBag(List.of(item, item2, item3, item4));

        Promotion promo = this.buildPromoSpecificProductsAll(
                "test",
                10D,
                SUBTOTAL_FIXED_AMOUNT,
                List.of(
                        buildPromoProdItem(1L, 1),
                        buildPromoProdItem(3L, 1),
                        buildPromoProdItem(4L, 1),

                        //Not present in cart
                        buildPromoProdItem(999L, 1)
                )
        );

        when(promotionRepository.findAllFetchItems()).thenReturn(List.of(promo));

        this.promotionService.init();
        this.promotionService.calculateDiscounts(priceBag, REGULAR);

        // Not all required prods were in the cart, ignoring this discount!
        assertEquals(0D, priceBag.sumAllDiscounts());
        assertEquals(0, priceBag.getAllDiscounts().size());
    }

    @Test
    public void testSpecificProductAll_fixedSubtotalDiscount_singleProductMinQtyNotMet() {
        ShoppingCartItemDetailedDto item = createItem(1L, 100D, 1);
        PriceBag priceBag = createPriceBag(item);

        Promotion promo = this.buildPromoSpecificProductsAll(
                "test",
                17D,
                SUBTOTAL_FIXED_AMOUNT,

                // qty is 2, but cart above says only 1 added
                List.of(buildPromoProdItem(1L, 2))
        );

        when(promotionRepository.findAllFetchItems()).thenReturn(List.of(promo));

        this.promotionService.init();
        this.promotionService.calculateDiscounts(priceBag, REGULAR);

        assertEquals(0D, priceBag.sumAllDiscounts());
        assertEquals(0, priceBag.getAllDiscounts().size());
    }

    @Test
    public void testSpecificProductAll_fixedPerProd_multiProdCart_oneOfTheQtysNotMet() {
        ShoppingCartItemDetailedDto item = createItem(1L, 100D, 1);
        ShoppingCartItemDetailedDto item2 = createItem(2L, 120D, 1);
        ShoppingCartItemDetailedDto item3 = createItem(3L, 130D, 1);
        ShoppingCartItemDetailedDto item4 = createItem(4L, 140D, 1);
        PriceBag priceBag = createPriceBag(List.of(item, item2, item3, item4));

        Promotion promo = this.buildPromoSpecificProductsAll(
                "test",
                10D,
                FIXED_AMOUNT_PER_PRODUCT,
                List.of(
                        buildPromoProdItem(1L, 1),
                        buildPromoProdItem(3L, 1),

                        // This Qty is not met
                        buildPromoProdItem(4L, 2)
                )
        );

        when(promotionRepository.findAllFetchItems()).thenReturn(List.of(promo));

        this.promotionService.init();
        this.promotionService.calculateDiscounts(priceBag, REGULAR);

        // Regardless that all products are there, if one qty is not met, ignore it
        assertEquals(0D, priceBag.sumAllDiscounts());
        assertEquals(0, priceBag.getAllDiscounts().size());
    }

    @Test
    public void testSpecificProductAll_fixedPerProd_emptyCartOrEmptyListOfItems() {
        ShoppingCartItemDetailedDto item4 = createItem(4L, 140D, 1);
        PriceBag priceBag = createPriceBag(List.of(item4));

        Promotion promo = this.buildPromoSpecificProductsAll(
                "test",
                10D,
                FIXED_AMOUNT_PER_PRODUCT,
                List.of(
                        //buildPromoProdItem(1L, 1)
                )
        );

        when(promotionRepository.findAllFetchItems()).thenReturn(List.of(promo));

        this.promotionService.init();
        this.promotionService.calculateDiscounts(priceBag, REGULAR);

        assertEquals(0D, priceBag.sumAllDiscounts());
        assertEquals(0, priceBag.getAllDiscounts().size());

        priceBag = createPriceBag(List.of());
        promo = this.buildPromoSpecificProductsAll(
                "test",
                10D,
                FIXED_AMOUNT_PER_PRODUCT,
                List.of(
                        buildPromoProdItem(1L, 1)
                )
        );

        when(promotionRepository.findAllFetchItems()).thenReturn(List.of(promo));
        this.promotionService.init();

        this.promotionService.calculateDiscounts(priceBag, REGULAR);

        assertEquals(0D, priceBag.sumAllDiscounts());
        assertEquals(0, priceBag.getAllDiscounts().size());
        assertEquals(0, priceBag.getAllDiscounts().size());
    }

    @Test
    public void testSpecificProductAny_fixedPerProd_singleProductMatch() {
        ShoppingCartItemDetailedDto item = createItem(1L, 100D, 1);
        PriceBag priceBag = createPriceBag(item);

        Promotion promo = this.buildPromoSpecificProductsAny(
                "test",
                18D,
                PERCENT_PER_PRODUCT,
                List.of(
                        buildPromoProdItem(1L, 1),
                        buildPromoProdItem(2L, 3)
                )
        );

        when(promotionRepository.findAllFetchItems()).thenReturn(List.of(promo));

        this.promotionService.init();
        this.promotionService.calculateDiscounts(priceBag, REGULAR);

        // Even though not all products are present, discount is still applied
        assertEquals(18D, priceBag.sumAllDiscounts());
        assertEquals(1, priceBag.getAllDiscounts().size());
        assertEquals(1, priceBag.getAllDiscounts().get(0).getProductItems().size());
    }

    @Test
    public void testSpecificProductAny_fixedPerProd_multiProduct() {
        ShoppingCartItemDetailedDto item = createItem(1L, 100D, 1);
        ShoppingCartItemDetailedDto item2 = createItem(2L, 120D, 1);
        ShoppingCartItemDetailedDto item3 = createItem(3L, 130D, 1);
        ShoppingCartItemDetailedDto item4 = createItem(4L, 140D, 1);
        PriceBag priceBag = createPriceBag(List.of(item, item2, item3, item4));

        Promotion promo = this.buildPromoSpecificProductsAny(
                "test",
                10D,
                FIXED_AMOUNT_PER_PRODUCT,
                List.of(
                        buildPromoProdItem(1L, 1),
                        buildPromoProdItem(3L, 1),
                        buildPromoProdItem(2L, 3)
                )
        );

        when(promotionRepository.findAllFetchItems()).thenReturn(List.of(promo));

        this.promotionService.init();
        this.promotionService.calculateDiscounts(priceBag, REGULAR);

        assertEquals(20D, priceBag.sumAllDiscounts());
        assertEquals(1, priceBag.getAllDiscounts().size());
        assertEquals(2, priceBag.getAllDiscounts().get(0).getProductItems().size());
    }

    @Test
    public void testCategory_subtotal_singleProduct() {
        final long catId = 321;

        ShoppingCartItemDetailedDto item = createItem(1L, 100D, 1, catId);
        PriceBag priceBag = createPriceBag(item);

        Promotion promo = this.buildPromoCategory(
                "test",
                19D,
                SUBTOTAL_FIXED_AMOUNT,
                catId
        );

        when(promotionRepository.findAllFetchItems()).thenReturn(List.of(promo));

        this.promotionService.init();
        this.promotionService.calculateDiscounts(priceBag, REGULAR);

        assertEquals(19D, priceBag.sumAllDiscounts());
        assertEquals(1, priceBag.getAllDiscounts().size());

        item.getProduct().setCategoryId(11111L);
        this.promotionService.calculateDiscounts(priceBag, REGULAR);
        priceBag = createPriceBag(item);
        // No items from that category, ignore promo
        assertEquals(0D, priceBag.sumAllDiscounts());
        assertEquals(0, priceBag.getAllDiscounts().size());
    }

    @Test
    public void testCategory_fixedPerProd_multiProd() {
        final long catId = 321;

        ShoppingCartItemDetailedDto item = createItem(1L, 100D, 1, catId);
        ShoppingCartItemDetailedDto item2 = createItem(2L, 120D, 1, catId);
        ShoppingCartItemDetailedDto item3 = createItem(3L, 130D, 1, 23L);
        ShoppingCartItemDetailedDto item4 = createItem(4L, 140D, 1, catId);
        ShoppingCartItemDetailedDto item5 = createItem(5L, 140D, 1, catId);
        ShoppingCartItemDetailedDto item6 = createItem(6L, 140D, 1, 22L);
        PriceBag priceBag = createPriceBag(List.of(item, item2, item3, item4, item5, item6));

        Promotion promo = this.buildPromoCategory(
                "test",
                11D,
                FIXED_AMOUNT_PER_PRODUCT,
                catId
        );

        when(promotionRepository.findAllFetchItems()).thenReturn(List.of(promo));

        this.promotionService.init();
        this.promotionService.calculateDiscounts(priceBag, REGULAR);

        assertEquals(44D, priceBag.sumAllDiscounts());
        assertEquals(1, priceBag.getAllDiscounts().size());
        assertEquals(4, priceBag.getAllDiscounts().get(0).getProductItems().size());
    }

    @Test
    public void testPercentPerProductDiscounter_singleQty() {
        PriceBag priceBag = createPriceBag(createItem(1L, 150D, 1));

        Promotion promotion = this.buildPromoSpecificProductsAll(
                "test", 20D, PERCENT_PER_PRODUCT,
                List.of(buildPromoProdItem(1L, 1))
        );

        when(promotionRepository.findAllFetchItems()).thenReturn(List.of(promotion));

        this.promotionService.init();
        this.promotionService.calculateDiscounts(priceBag, REGULAR);

        // 20 % of 150 is 30
        assertEquals(30D, priceBag.sumAllDiscounts());
    }

    @Test
    public void testPercentPerProductDiscounter_multiQty() {
        PriceBag priceBag = createPriceBag(createItem(1L, 115D, 5));

        Promotion promotion = this.buildPromoSpecificProductsAll(
                "test", 13.05D, PERCENT_PER_PRODUCT,
                List.of(buildPromoProdItem(1L, 1))
        );

        when(promotionRepository.findAllFetchItems()).thenReturn(List.of(promotion));

        this.promotionService.init();
        this.promotionService.calculateDiscounts(priceBag, REGULAR);

        // 5 * 15 % of 115
        assertEquals(75D, priceBag.sumAllDiscounts());
    }

    @Test
    public void testPercentPerProductDiscounter_multiQtyAndMultiDiscounts() {
        PriceBag priceBag = createPriceBag(createItem(1L, 115D, 2));

        Promotion promotion = this.buildPromoSpecificProductsAll(
                "test", 13.05D, PERCENT_PER_PRODUCT,
                List.of(buildPromoProdItem(1L, 1))
        );

        Promotion promotion2 = this.buildPromoSpecificProductsAll(
                "test", 10D, PERCENT_PER_PRODUCT,
                List.of(buildPromoProdItem(1L, 1))
        );

        when(promotionRepository.findAllFetchItems()).thenReturn(List.of(promotion, promotion2));

        this.promotionService.init();
        this.promotionService.calculateDiscounts(priceBag, REGULAR);

        // 2 * 10 % of 115 + 2 * 13.05 % of 115
        assertEquals(53D, priceBag.sumAllDiscounts());
        assertEquals(2, priceBag.getAllDiscounts().size());
    }

    @Test
    public void testFixedAmountAndPercentPerProductDiscounter_multiQtyAndMultiDiscounts() {
        PriceBag priceBag = createPriceBag(createItem(1L, 115D, 2));

        Promotion promotion = this.buildPromoSpecificProductsAll(
                "test", 13.05D, PERCENT_PER_PRODUCT,
                List.of(buildPromoProdItem(1L, 1))
        );

        Promotion promotion2 = this.buildPromoSpecificProductsAll(
                "test", 10D, FIXED_AMOUNT_PER_PRODUCT,
                List.of(buildPromoProdItem(1L, 1))
        );

        when(promotionRepository.findAllFetchItems()).thenReturn(List.of(promotion, promotion2));

        this.promotionService.init();
        this.promotionService.calculateDiscounts(priceBag, REGULAR);

        // 2 * 10 + 2 * 13.05 % of 115
        assertEquals(50D, priceBag.sumAllDiscounts());
        assertEquals(2, priceBag.getAllDiscounts().size());
    }

    @Test
    public void testFreeDeliveryDiscount() {
        PriceBag priceBag = createPriceBag(createItem(1L, 115D, 5));
        assertFalse(priceBag.isFreeDelivery());

        Promotion promotion = this.buildPromo(
                DISCOUNT_OVER_SUBTOTAL, FREE_DELIVERY, "test 1", null
        );
        promotion.setMinSubtotal(5D);

        when(promotionRepository.findAllFetchItems()).thenReturn(List.of(promotion));

        this.promotionService.init();
        this.promotionService.calculateDiscounts(priceBag, REGULAR);

        assertTrue(priceBag.isFreeDelivery());
        assertEquals(0, priceBag.getAllDiscounts().size());

        // subtotal not met, no free delivery
        priceBag = createPriceBag(createItem(1L, 115D, 5));
        assertFalse(priceBag.isFreeDelivery());
        promotion.setMinSubtotal(555555D);
        this.promotionService.calculateDiscounts(priceBag, REGULAR);
        assertFalse(priceBag.isFreeDelivery());
        assertEquals(0, priceBag.getAllDiscounts().size());


    }

    @Test
    public void testGetFreeDeliveryThreshold_MultiplePromos_ShouldGetTheCustomerBest() {
        Promotion promotion1 = this.buildPromo(
                DISCOUNT_OVER_SUBTOTAL, FREE_DELIVERY, "fd 1", null
        );
        Promotion promotion2 = this.buildPromo(
                DISCOUNT_OVER_SUBTOTAL, FREE_DELIVERY, "fd 2", null
        );
        Promotion promotion3 = this.buildPromo(
                DISCOUNT_OVER_SUBTOTAL, FREE_DELIVERY, "fd 3", null
        );

        promotion1.setMinSubtotal(10D);
        promotion2.setMinSubtotal(9D);
        promotion3.setMinSubtotal(11D);

        when(promotionRepository.findAllFetchItems()).thenReturn(
                List.of(promotion1, promotion2, promotion3));

        this.promotionService.reloadCachedPromotions();
        final Double threshold = this.promotionService.getFreeDeliveryThreshold(4D, 0D);
        assertEquals(5D, threshold);
    }

    @Test
    public void testGetFreeDeliveryThreshold_PromoOverSubtotal_ShouldUseSubtotalToCalculate() {
        Promotion promotion1 = this.buildPromo(
                DISCOUNT_OVER_SUBTOTAL, FREE_DELIVERY, "fd 1", null
        );

        promotion1.setMinSubtotal(100D);

        when(promotionRepository.findAllFetchItems()).thenReturn(List.of(promotion1));

        this.promotionService.reloadCachedPromotions();
        final Double threshold = this.promotionService.getFreeDeliveryThreshold(5D, 50D);
        assertEquals(95D, threshold);
    }

    @Test
    public void testGetFreeDeliveryThreshold_PromoOverTotal_ShouldUseSubtotalToCalculate() {
        Promotion promotion1 = this.buildPromo(
                DISCOUNT_OVER_TOTAL, FREE_DELIVERY, "fd 1", null
        );

        promotion1.setMinSubtotal(100D);

        when(promotionRepository.findAllFetchItems()).thenReturn(List.of(promotion1));

        this.promotionService.reloadCachedPromotions();
        final Double threshold = this.promotionService.getFreeDeliveryThreshold(5D, 50D);
        assertEquals(50D, threshold);
    }

    @Test
    public void testGetFreeDeliveryThreshold_NoFreeDeliveryPromos_ShouldReturnNull() {
        when(promotionRepository.findAllFetchItems()).thenReturn(List.of());

        this.promotionService.reloadCachedPromotions();
        final Double threshold = this.promotionService.getFreeDeliveryThreshold(5D, 50D);
        assertNull(threshold);
    }

    @Test
    public void testGetFreeDeliveryThreshold_PromoNeitherTotalOrSubtotal_ShouldReturnNull() {
        Promotion promotion1 = this.buildPromoSpecificProductsAny(
                "fd 1",
                500D,
                FREE_DELIVERY,
                List.of(this.buildPromoProdItem(1L, 2))
        );

        promotion1.setMinSubtotal(100D);

        when(promotionRepository.findAllFetchItems()).thenReturn(List.of(promotion1));

        this.promotionService.reloadCachedPromotions();
        final Double threshold = this.promotionService.getFreeDeliveryThreshold(5D, 50D);
        assertNull(threshold);
    }

    private Promotion buildPromoCategory(
            String name,
            double discount,
            DiscountType discountType,
            long catId) {
        Promotion promo = buildPromo(
                DISCOUNT_SPECIFIC_CATEGORY,
                discountType,
                name,
                discount
        );

        promo.setCategoryId(catId);

        return promo;
    }

    private Promotion buildPromoSpecificProductsAny(
            String name,
            Double discount,
            DiscountType discountType,
            List<PromotionProductItem> promoProdItems) {
        Promotion promo = buildPromo(
                DISCOUNT_SPECIFIC_PRODUCTS_ANY,
                discountType,
                name,
                discount
        );

        promo.setProductItems(promoProdItems);

        return promo;
    }

    private Promotion buildPromoSpecificProductsAll(
            String name,
            Double discount,
            DiscountType discountType,
            List<PromotionProductItem> promoProdItems) {
        Promotion promo = buildPromo(
                DISCOUNT_SPECIFIC_PRODUCTS_ALL,
                discountType,
                name,
                discount
        );

        promo.setProductItems(promoProdItems);

        return promo;
    }

    private Promotion buildPromoSubtotal_subtotal(String name, Double discount,
            Double minSubtotal) {
        Promotion promo = buildPromo(DISCOUNT_OVER_SUBTOTAL, SUBTOTAL_FIXED_AMOUNT, name,
                discount);

        promo.setMinSubtotal(minSubtotal);

        return promo;
    }

    private Promotion buildPromoTotal_subtotal(String name, Double discount,
            Double minTotal) {
        Promotion promo = buildPromo(DISCOUNT_OVER_TOTAL, SUBTOTAL_FIXED_AMOUNT, name,
                discount);

        promo.setMinSubtotal(minTotal);

        return promo;
    }

    private Promotion buildPromo(PromotionType type,
            DiscountType discountType,
            String name,
            Double discount) {
        final Promotion promotion = new Promotion();
        promotion.setPromotionType(type);
        promotion.setDiscountType(discountType);
        promotion.setNameBg(name);
        promotion.setDiscount(discount);

        return promotion;
    }

    private ShoppingCartItemDetailedDto createItem(Long id, Double price, int qty) {
        final ProductDto productDto = new ProductDto();
        productDto.setId(id);
        productDto.setPrice(price);
        productDto.setQuantity(qty);

        return this.createItem(id, price, qty, null);
    }

    private ShoppingCartItemDetailedDto createItem(long id, Double price, int qty, Long catId) {
        final ProductDto productDto = new ProductDto();
        productDto.setId(id);
        productDto.setPrice(price);
        productDto.setQuantity(qty);
        productDto.setCategoryId(catId);

        return new ShoppingCartItemDetailedDto(
                productDto,
                qty,
                MathUtil.calculatePrice(price, qty)
        );
    }

    private PromotionProductItem buildPromoProdItem(long id, int minQty) {
        return new PromotionProductItem() {{
            setMinQuantity(minQty);
            setProductId(id);
        }};
    }

    private PriceBag createPriceBag(List<ShoppingCartItemDetailedDto> items) {
        return new PriceBag(
                new ShoppingCartDetailedDto(LocalDateTime.now(), null, items),
                items.stream().map(ShoppingCartItemDetailedDto::getCalculatedPrice)
                        .reduce(Double::sum).orElse(0D));
    }

    private PriceBag createPriceBag(ShoppingCartItemDetailedDto item) {
        return createPriceBag(List.of(item));
    }
}
