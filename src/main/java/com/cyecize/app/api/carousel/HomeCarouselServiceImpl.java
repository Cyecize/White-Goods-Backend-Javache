package com.cyecize.app.api.carousel;

import com.cyecize.app.api.base64.Base64FileService;
import com.cyecize.app.util.Specification;
import com.cyecize.app.util.SpecificationExecutor;
import com.cyecize.summer.common.annotations.Service;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeCarouselServiceImpl implements HomeCarouselService {

    private final SpecificationExecutor specificationExecutor;

    private final HomeCarouselRepository homeCarouselRepository;

    private final ModelMapper modelMapper;

    private final Base64FileService base64FileService;

    @Override
    public List<HomeCarousel> getHomeCarousel() {
        final Specification<HomeCarousel> specification = HomeCarouselSpecifications.enabled(true)
                .and(HomeCarouselSpecifications.applyOrder());

        return this.specificationExecutor.findAll(specification, HomeCarousel.class, null);
    }

    @Override
    public HomeCarousel createCarouselItem(CreateCarouselDto dto) {
        final HomeCarousel homeCarousel = this.modelMapper.map(dto, HomeCarousel.class);
        final String imagePath = this.base64FileService.saveFile(dto.getImage());

        homeCarousel.setImageUrl(imagePath);
        return this.homeCarouselRepository.persist(homeCarousel);
    }
}
