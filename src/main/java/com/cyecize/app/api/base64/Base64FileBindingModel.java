package com.cyecize.app.api.base64;

import com.cyecize.app.constants.General;
import com.cyecize.summer.areas.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Base64FileBindingModel {
    @NotNull
    @Base64FileSize(maxBytes = General.MAX_FILE_SIZE_BYTES)
    private String base64;

    @NotNull
    private String contentType;

    @NotNull
    private String fileName;
}
