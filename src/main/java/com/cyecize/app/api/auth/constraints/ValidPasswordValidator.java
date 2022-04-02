package com.cyecize.app.api.auth.constraints;

import com.cyecize.app.api.auth.LoginBindingModel;
import com.cyecize.summer.areas.validation.exceptions.ConstraintValidationException;
import com.cyecize.summer.areas.validation.interfaces.ConstraintValidator;
import com.cyecize.summer.common.annotations.Component;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;

@Component
@RequiredArgsConstructor
public class ValidPasswordValidator implements ConstraintValidator<ValidPassword, String> {
    @Override
    public boolean isValid(String password, Object bindingModel) {
        if (!(bindingModel instanceof LoginBindingModel)) {
            throw new ConstraintValidationException("Validator only supports " + LoginBindingModel.class.getName());
        }

        final LoginBindingModel loginBindingModel = (LoginBindingModel) bindingModel;
        if (loginBindingModel.getUser() == null) {
            return true;
        }

        return BCrypt.checkpw(loginBindingModel.getPassword(), loginBindingModel.getUser().getPassword());
    }
}
