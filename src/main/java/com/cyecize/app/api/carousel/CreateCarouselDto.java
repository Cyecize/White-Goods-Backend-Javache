package com.cyecize.app.api.carousel;

import com.cyecize.app.api.base64.validator.ImageTypeFile;
import com.cyecize.app.constants.General;
import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.app.api.base64.Base64FileBindingModel;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.areas.validation.constraints.MaxLength;
import com.cyecize.summer.areas.validation.constraints.NotEmpty;
import com.cyecize.summer.areas.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCarouselDto {
    private Long productId;

    @NotEmpty(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    @MaxLength(length = General.MAX_VARCHAR, message = ValidationMessages.INVALID_VALUE)
    private String textEn;

    @NotEmpty(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    @MaxLength(length = General.MAX_VARCHAR, message = ValidationMessages.INVALID_VALUE)
    private String textBg;

    @Valid
    @NotNull(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    @ImageTypeFile(message = ValidationMessages.INVALID_VALUE)
    private Base64FileBindingModel image;

    @MaxLength(length = General.MAX_VARCHAR, message = ValidationMessages.INVALID_VALUE)
    private String customLink;

    private Boolean customLinkSamePage;

    private Boolean enabled;

    @NotNull(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    private Integer orderNumber;
}
