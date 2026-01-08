package com.cyecize.app.api.product.dto;

import com.cyecize.app.api.base64.Base64FileBindingModel;
import com.cyecize.app.api.base64.validator.ImageTypeFile;
import com.cyecize.app.constants.General;
import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.areas.validation.constraints.MaxLength;
import com.cyecize.summer.areas.validation.constraints.NotEmpty;
import com.cyecize.summer.areas.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class CreateCategoryDto {

    @NotEmpty(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    @MaxLength(length = General.MAX_VARCHAR, message = ValidationMessages.INVALID_VALUE)
    private String nameEn;

    @NotEmpty(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    @MaxLength(length = General.MAX_VARCHAR, message = ValidationMessages.INVALID_VALUE)
    private String nameBg;

    @Valid
    @NotNull(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    @ImageTypeFile(message = ValidationMessages.INVALID_VALUE)
    private Base64FileBindingModel image;

    @MaxLength(length = General.MAX_ALLOWED_TAGS, message = ValidationMessages.TOO_MANY_TAGS)
    private List<String> tagNames;
}
