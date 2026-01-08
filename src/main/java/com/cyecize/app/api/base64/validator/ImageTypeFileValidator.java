package com.cyecize.app.api.base64.validator;

import com.cyecize.app.api.base64.Base64FileBindingModel;
import com.cyecize.summer.areas.validation.interfaces.ConstraintValidator;
import com.cyecize.summer.common.annotations.Component;
import java.util.List;

@Component
public class ImageTypeFileValidator implements
        ConstraintValidator<ImageTypeFile, Base64FileBindingModel> {

    @Override
    public boolean isValid(Base64FileBindingModel field, Object bindingModel) {
        if (field == null) {
            return true;
        }

        final String contentType = field.getContentType() == null ? "" : field.getContentType();
        return List.of("image/png", "image/jpeg", "image/jpg").contains(contentType);
    }
}
