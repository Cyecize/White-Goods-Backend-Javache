package com.cyecize.app.api.carousel;

import com.cyecize.app.api.base64.Base64FileService;
import com.cyecize.app.api.user.User;
import com.cyecize.app.util.Specification;
import com.cyecize.app.util.SpecificationExecutor;
import com.cyecize.summer.common.annotations.Service;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@Service
@RequiredArgsConstructor
public class HomeCarouselServiceImpl implements HomeCarouselService {

    private final SpecificationExecutor specificationExecutor;

    private final HomeCarouselRepository homeCarouselRepository;

    private final ModelMapper modelMapper;

    private final Base64FileService base64FileService;

    @Override
    public List<HomeCarousel> getHomeCarousel(boolean showHidden, User currentUser) {
        final Specification<HomeCarousel> specification = HomeCarouselSpecifications.showHidden(
                        showHidden, currentUser)
                .and(HomeCarouselSpecifications.applyOrder());

        return this.specificationExecutor.findAll(specification, HomeCarousel.class, null);
    }

    @Override
    public HomeCarousel createCarouselItem(CreateCarouselDto dto) {
        final HomeCarousel homeCarousel = this.modelMapper.map(dto, HomeCarousel.class);
        final String imagePath = this.base64FileService.saveFile(dto.getImage());
        homeCarousel.setImageUrl(imagePath);

        if (dto.getImageMobile() != null) {
            final String imageMobilePath = this.base64FileService.saveFile(dto.getImageMobile());
            homeCarousel.setImageMobileUrl(imageMobilePath);
        }

        return this.homeCarouselRepository.persist(homeCarousel);
    }

    @Override
    public HomeCarousel editCarouselItem(Long id, EditCarouselDto dto) {
        final HomeCarousel homeCarousel = this.getCarouselItem(id);

        //TODO: ModelMerger
        homeCarousel.setEnabled(dto.getEnabled());
        homeCarousel.setTextBg(dto.getTextBg());
        homeCarousel.setTextEn(dto.getTextEn());
        homeCarousel.setOrderNumber(dto.getOrderNumber());
        homeCarousel.setCustomLink(dto.getCustomLink());
        homeCarousel.setCustomLinkSamePage(dto.getCustomLinkSamePage());
        homeCarousel.setProductId(dto.getProductId());

        if (dto.getImage() != null) {
            this.base64FileService.removeFile(homeCarousel.getImageUrl());
            final String imagePath = this.base64FileService.saveFile(dto.getImage());
            homeCarousel.setImageUrl(imagePath);
        }

        if (dto.getImageMobile() != null) {
            if (homeCarousel.getImageMobileUrl() != null) {
                this.base64FileService.removeFile(homeCarousel.getImageMobileUrl());
            }

            final String imageMobilePath = this.base64FileService.saveFile(dto.getImageMobile());
            homeCarousel.setImageMobileUrl(imageMobilePath);
        }

        this.homeCarouselRepository.merge(homeCarousel);
        return homeCarousel;
    }

    @Override
    public HomeCarousel getCarouselItem(Long id) {
        return this.specificationExecutor.findOne(
                HomeCarouselSpecifications.idEquals(id), HomeCarousel.class, null
        );
    }
}
