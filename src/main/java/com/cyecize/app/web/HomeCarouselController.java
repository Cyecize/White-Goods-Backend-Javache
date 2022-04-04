package com.cyecize.app.web;

import com.cyecize.app.api.carousel.CreateCarouselDto;
import com.cyecize.app.api.carousel.HomeCarouselDto;
import com.cyecize.app.api.carousel.HomeCarouselService;
import com.cyecize.app.constants.Endpoints;
import com.cyecize.app.constants.General;
import com.cyecize.summer.areas.security.annotations.PreAuthorize;
import com.cyecize.summer.areas.security.enums.AuthorizationType;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.GetMapping;
import com.cyecize.summer.common.annotations.routing.PostMapping;
import com.cyecize.summer.common.annotations.routing.RequestMapping;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "", produces = General.APPLICATION_JSON)
@PreAuthorize(role = General.ROLE_ADMIN)
@RequiredArgsConstructor
public class HomeCarouselController {

    private final HomeCarouselService homeCarouselService;

    private final ModelMapper modelMapper;

    @GetMapping(Endpoints.HOME_CAROUSEL)
    @PreAuthorize(AuthorizationType.ANY)
    public List<HomeCarouselDto> getHomeCarousel() {
        return this.homeCarouselService.getHomeCarousel().stream()
                .map(hc -> this.modelMapper.map(hc, HomeCarouselDto.class))
                .collect(Collectors.toList());
    }

    @PostMapping(Endpoints.HOME_CAROUSEL)
    public HomeCarouselDto createCarouselItem(@Valid CreateCarouselDto dto) {
        return this.modelMapper.map(this.homeCarouselService.createCarouselItem(dto), HomeCarouselDto.class);
    }
}
