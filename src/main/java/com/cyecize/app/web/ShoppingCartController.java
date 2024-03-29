package com.cyecize.app.web;

import com.cyecize.app.api.store.cart.AddShoppingCartItemDto;
import com.cyecize.app.api.store.cart.ShoppingCartDetailedDto;
import com.cyecize.app.api.store.cart.ShoppingCartService;
import com.cyecize.app.api.store.pricing.Price;
import com.cyecize.app.api.store.pricing.PricingService;
import com.cyecize.app.api.store.promotion.coupon.validators.ValidCouponCode;
import com.cyecize.app.constants.Endpoints;
import com.cyecize.app.constants.General;
import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.http.HttpStatus;
import com.cyecize.summer.areas.security.annotations.PreAuthorize;
import com.cyecize.summer.areas.security.enums.AuthorizationType;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.areas.validation.constraints.MaxLength;
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.DeleteMapping;
import com.cyecize.summer.common.annotations.routing.GetMapping;
import com.cyecize.summer.common.annotations.routing.PathVariable;
import com.cyecize.summer.common.annotations.routing.PostMapping;
import com.cyecize.summer.common.annotations.routing.RequestMapping;
import com.cyecize.summer.common.annotations.routing.RequestParam;
import com.cyecize.summer.common.models.JsonResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Controller
@PreAuthorize(AuthorizationType.ANY)
@RequestMapping(value = "", produces = General.APPLICATION_JSON)
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final PricingService pricingService;

    @PostMapping(Endpoints.SHOPPING_CART_SESSION)
    public SessionDto createShoppingSession() {
        return new SessionDto(this.shoppingCartService.createSession());
    }

    @GetMapping(Endpoints.SHOPPING_CART)
    public ShoppingCartDetailedDto getShoppingCart(
            @PathVariable("session") String session,
            @RequestParam(value = "syncWithAccount") boolean mergeDb) {
        return this.shoppingCartService.getShoppingCart(session, mergeDb);
    }

    @PostMapping(Endpoints.SHOPPING_CART)
    public ShoppingCartDetailedDto addItemToCart(
            @PathVariable("session") String session,
            @Valid AddShoppingCartItemDto dto) {
        return this.shoppingCartService.addItem(session, dto);
    }

    @DeleteMapping(Endpoints.SHOPPING_CART)
    public JsonResponse clearShoppingCart(@PathVariable("session") String session) {
        this.shoppingCartService.removeAllItems(session);
        return new JsonResponse()
                .setStatusCode(HttpStatus.OK)
                .addAttribute("message", "Shopping cart cleared!");
    }

    @DeleteMapping(Endpoints.SHOPPING_CART_ITEM)
    public ShoppingCartDetailedDto removeItem(
            @PathVariable("session") String session,
            @PathVariable("prodId") Long productId) {
        return this.shoppingCartService.removeItem(session, productId);
    }

    @GetMapping(Endpoints.SHOPPING_CART_PRICING)
    public Price getPricing(@PathVariable("session") String session) {
        return this.pricingService.getPrice(session);
    }

    @PostMapping(Endpoints.SHOPPING_CART_COUPON_CODE)
    public ShoppingCartDetailedDto applyCouponCode(@PathVariable("session") String session,
            @Valid ApplyCouponCodeDto dto) {
        return this.shoppingCartService.applyCouponCode(session, dto.getCode());
    }

    @DeleteMapping(Endpoints.SHOPPING_CART_COUPON_CODE)
    public ShoppingCartDetailedDto removeCouponCode(@PathVariable("session") String session) {
        return this.shoppingCartService.removeCouponCode(session);
    }

    @Data
    static class SessionDto {

        private final String sessionId;
    }

    @Data
    public static class ApplyCouponCodeDto {

        @Valid
        @ValidCouponCode
        @MaxLength(length = General.MAX_NAME, message = ValidationMessages.INVALID_VALUE)
        private String code;
    }
}
