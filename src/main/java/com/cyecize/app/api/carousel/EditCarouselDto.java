package com.cyecize.app.api.carousel;

import com.cyecize.app.api.base64.Base64FileBindingModel;
import com.cyecize.app.api.base64.validator.ImageTypeFile;
import com.cyecize.app.constants.General;
import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.areas.validation.constraints.Max;
import com.cyecize.summer.areas.validation.constraints.MaxLength;
import com.cyecize.summer.areas.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EditCarouselDto {

    private Long productId;

    @MaxLength(length = General.MAX_VARCHAR, message = ValidationMessages.INVALID_VALUE)
    private String textEn;

    @MaxLength(length = General.MAX_VARCHAR, message = ValidationMessages.INVALID_VALUE)
    private String textBg;

    @Valid
    @ImageTypeFile(message = ValidationMessages.INVALID_VALUE)
    private Base64FileBindingModel image;

    @Valid
    @ImageTypeFile(message = ValidationMessages.INVALID_VALUE)
    private Base64FileBindingModel imageMobile;

    @MaxLength(length = General.MAX_VARCHAR, message = ValidationMessages.INVALID_VALUE)
    private String customLink;

    private Boolean customLinkSamePage;

    private Boolean enabled;

    @NotNull(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    private Integer orderNumber;

    @NotNull(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    @Max(value = 100, message = ValidationMessages.INVALID_VALUE)
    private Integer dim;
}
