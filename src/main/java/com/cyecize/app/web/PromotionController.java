package com.cyecize.app.web;

import com.cyecize.app.api.store.promotion.PromotionService;
import com.cyecize.app.api.store.promotion.dto.PromotionDto;
import com.cyecize.app.api.store.promotion.dto.PromotionQuery;
import com.cyecize.app.constants.Endpoints;
import com.cyecize.app.constants.General;
import com.cyecize.app.util.Page;
import com.cyecize.summer.areas.security.annotations.PreAuthorize;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.GetMapping;
import com.cyecize.summer.common.annotations.routing.PostMapping;
import com.cyecize.summer.common.annotations.routing.RequestMapping;
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
}
