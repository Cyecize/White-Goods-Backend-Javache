package com.cyecize.app.web;

import com.cyecize.app.api.store.promotion.Promotion;
import com.cyecize.app.api.store.promotion.PromotionService;
import com.cyecize.app.api.store.promotion.converter.PromotionIdDataAdapter;
import com.cyecize.app.api.store.promotion.dto.CreatePromotionDto;
import com.cyecize.app.api.store.promotion.dto.PromotionDto;
import com.cyecize.app.api.store.promotion.dto.PromotionQuery;
import com.cyecize.app.constants.Endpoints;
import com.cyecize.app.constants.General;
import com.cyecize.app.error.NotFoundApiException;
import com.cyecize.app.util.Page;
import com.cyecize.http.HttpStatus;
import com.cyecize.summer.areas.security.annotations.PreAuthorize;
import com.cyecize.summer.areas.validation.annotations.ConvertedBy;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.DeleteMapping;
import com.cyecize.summer.common.annotations.routing.GetMapping;
import com.cyecize.summer.common.annotations.routing.PathVariable;
import com.cyecize.summer.common.annotations.routing.PostMapping;
import com.cyecize.summer.common.annotations.routing.PutMapping;
import com.cyecize.summer.common.annotations.routing.RequestMapping;
import com.cyecize.summer.common.models.JsonResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@Controller
@RequestMapping(value = "", produces = General.APPLICATION_JSON)
@PreAuthorize(role = General.ROLE_ADMIN)
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    private final ModelMapper modelMapper;

    @PostMapping(Endpoints.PROMOTIONS_SEARCH)
    public Page<PromotionDto> searchPromotions(@Valid PromotionQuery query) {
        return this.promotionService
                .searchPromotions(query)
                .map(promo -> this.modelMapper.map(promo, PromotionDto.class));
    }

    @PostMapping(Endpoints.PROMOTIONS)
    public PromotionDto createPromotion(@Valid CreatePromotionDto dto) {
        return this.promotionService.createPromotion(dto);
    }

    @DeleteMapping(Endpoints.PROMOTION)
    public JsonResponse deletePromo(
            @ConvertedBy(PromotionIdDataAdapter.class) @PathVariable("id") Promotion promotion) {
        this.promotionService.deletePromotion(promotion);
        return new JsonResponse(HttpStatus.OK)
                .addAttribute("message", "Promo removed successfully!");
    }

    @GetMapping(Endpoints.PROMOTION)
    public PromotionDto getPromotion(@PathVariable("id") Long promoId) {
        final Promotion promotion = this.promotionService.findPromoById(promoId);

        if (promotion == null) {
            throw new NotFoundApiException("Promo not found!");
        }

        return this.modelMapper.map(promotion, PromotionDto.class);
    }

    @PutMapping(Endpoints.PROMOTION)
    public PromotionDto editPromotion(
            @ConvertedBy(PromotionIdDataAdapter.class) @PathVariable("id") Promotion promotion,
            @Valid CreatePromotionDto dto) {
        return this.promotionService.editPromotion(promotion.getId(), dto);
    }
}
