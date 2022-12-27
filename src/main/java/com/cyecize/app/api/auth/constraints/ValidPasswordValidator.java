package com.cyecize.app.api.auth.constraints;

import com.cyecize.app.error.ApiException;
import com.cyecize.summer.areas.validation.interfaces.ConstraintValidator;
import com.cyecize.summer.common.annotations.Component;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;

@Component
@RequiredArgsConstructor
public class ValidPasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password, Object bindingModel) {
        if (!(bindingModel instanceof ValidPasswordDto)) {
            throw new ApiException(
                    "Validator only supports " + ValidPasswordDto.class.getName()
            );
        }

        final ValidPasswordDto validPasswordDto = (ValidPasswordDto) bindingModel;
        if (validPasswordDto.getUser() == null) {
            return true;
        }

        return BCrypt.checkpw(
                validPasswordDto.getPassword(),
                validPasswordDto.getUser().getPassword()
        );
    }
}
