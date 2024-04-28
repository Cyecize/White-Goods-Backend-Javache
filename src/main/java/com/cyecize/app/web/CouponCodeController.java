package com.cyecize.app.web;

import com.cyecize.app.api.store.promotion.coupon.CouponCodeDetailedDto;
import com.cyecize.app.api.store.promotion.coupon.CouponCodeDto;
import com.cyecize.app.api.store.promotion.coupon.CouponCodeQuery;
import com.cyecize.app.api.store.promotion.coupon.CouponCodeService;
import com.cyecize.app.api.store.promotion.coupon.CreateCouponCodeDto;
import com.cyecize.app.constants.Endpoints;
import com.cyecize.app.constants.General;
import com.cyecize.app.util.Page;
import com.cyecize.summer.areas.security.annotations.PreAuthorize;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.DeleteMapping;
import com.cyecize.summer.common.annotations.routing.PathVariable;
import com.cyecize.summer.common.annotations.routing.PostMapping;
import com.cyecize.summer.common.annotations.routing.RequestMapping;
import com.cyecize.summer.common.models.JsonResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@Controller
@RequestMapping(value = "", produces = General.APPLICATION_JSON)
@PreAuthorize(role = General.ROLE_ADMIN)
@RequiredArgsConstructor
public class CouponCodeController {

    private final CouponCodeService couponCodeService;
    private final ModelMapper modelMapper;

    @PostMapping(Endpoints.COUPON_CODES)
    public List<CouponCodeDto> createCouponCodes(@Valid CreateCouponCodeDto dto) {
        return this.couponCodeService.createCouponCodes(dto);
    }

    @PostMapping(Endpoints.COUPON_CODES_SEARCH)
    public Page<CouponCodeDetailedDto> searchCouponCodes(@Valid CouponCodeQuery query) {
        return this.couponCodeService.search(query)
                .map(code -> this.modelMapper.map(code, CouponCodeDetailedDto.class));
    }

    @DeleteMapping(Endpoints.COUPON_CODE)
    public JsonResponse deleteCode(@PathVariable("code") String code) {
        this.couponCodeService.deleteCode(code);
        return new JsonResponse().addAttribute("message", "Code is removed!");
    }
}
