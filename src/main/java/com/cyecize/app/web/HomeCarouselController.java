package com.cyecize.app.web;

import com.cyecize.app.api.carousel.CreateCarouselDto;
import com.cyecize.app.api.carousel.EditCarouselDto;
import com.cyecize.app.api.carousel.HomeCarousel;
import com.cyecize.app.api.carousel.HomeCarouselDto;
import com.cyecize.app.api.carousel.HomeCarouselIdDataAdapter;
import com.cyecize.app.api.carousel.HomeCarouselService;
import com.cyecize.app.api.user.User;
import com.cyecize.app.constants.Endpoints;
import com.cyecize.app.constants.General;
import com.cyecize.summer.areas.security.annotations.PreAuthorize;
import com.cyecize.summer.areas.security.enums.AuthorizationType;
import com.cyecize.summer.areas.security.models.Principal;
import com.cyecize.summer.areas.validation.annotations.ConvertedBy;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.GetMapping;
import com.cyecize.summer.common.annotations.routing.PathVariable;
import com.cyecize.summer.common.annotations.routing.PostMapping;
import com.cyecize.summer.common.annotations.routing.PutMapping;
import com.cyecize.summer.common.annotations.routing.RequestMapping;
import com.cyecize.summer.common.annotations.routing.RequestParam;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@Controller
@RequestMapping(value = "", produces = General.APPLICATION_JSON)
@PreAuthorize(role = General.ROLE_ADMIN)
@RequiredArgsConstructor
public class HomeCarouselController {

    private final HomeCarouselService homeCarouselService;

    private final ModelMapper modelMapper;

    @GetMapping(Endpoints.HOME_CAROUSEL)
    @PreAuthorize(AuthorizationType.ANY)
    public List<HomeCarouselDto> getHomeCarousel(@RequestParam("showHidden") boolean showHidden,
            Principal principal) {
        return this.homeCarouselService.getHomeCarousel(showHidden, (User) principal.getUser())
                .stream()
                .map(hc -> this.modelMapper.map(hc, HomeCarouselDto.class))
                .collect(Collectors.toList());
    }

    @PostMapping(Endpoints.HOME_CAROUSEL)
    public HomeCarouselDto createCarouselItem(@Valid CreateCarouselDto dto) {
        return this.modelMapper.map(this.homeCarouselService.createCarouselItem(dto),
                HomeCarouselDto.class);
    }

    @PutMapping(Endpoints.HOME_CAROUSEL_ITEM)
    public HomeCarouselDto editHomeCarousel(@PathVariable("id")
            @ConvertedBy(HomeCarouselIdDataAdapter.class) HomeCarousel item,
            @Valid EditCarouselDto dto) {
        return this.modelMapper.map(this.homeCarouselService.editCarouselItem(item.getId(), dto),
                HomeCarouselDto.class);
    }

    @GetMapping(Endpoints.HOME_CAROUSEL_ITEM)
    public HomeCarouselDto getCarousel(@PathVariable("id")
    @ConvertedBy(HomeCarouselIdDataAdapter.class) HomeCarousel item) {
        return this.modelMapper.map(item, HomeCarouselDto.class);
    }
}
