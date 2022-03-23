package com.cyecize.app.util;

import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.summer.areas.validation.constraints.NotEmpty;
import com.cyecize.summer.areas.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SortQuery {

    @NotEmpty(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    private String field;

    @NotNull(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    private SortDirection direction;
}
