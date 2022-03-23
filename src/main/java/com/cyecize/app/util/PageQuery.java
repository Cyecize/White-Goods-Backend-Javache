package com.cyecize.app.util;

import com.cyecize.summer.areas.validation.constraints.Min;
import com.cyecize.summer.areas.validation.constraints.NotNull;
import lombok.Data;

import static com.cyecize.app.constants.ValidationMessages.FIELD_CANNOT_BE_NULL;

@Data
public class PageQuery {

    @NotNull(message = FIELD_CANNOT_BE_NULL)
    @Min(0)
    private Integer page;

    @NotNull(message = FIELD_CANNOT_BE_NULL)
    @Min(1)
    private Integer size;
}
