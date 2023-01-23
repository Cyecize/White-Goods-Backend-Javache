package com.cyecize.app.api.carousel;

import lombok.Data;

@Data
public class HomeCarouselDto {
    private Long id;

    private Long productId;

    private String textEn;

    private String textBg;

    private String imageUrl;

    private String imageMobileUrl;

    private String customLink;

    private Boolean enabled;

    private Boolean customLinkSamePage;

    private Integer orderNumber;
}
