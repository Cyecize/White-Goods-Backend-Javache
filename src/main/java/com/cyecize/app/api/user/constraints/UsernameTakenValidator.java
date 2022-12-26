package com.cyecize.app.api.user.constraints;

import com.cyecize.app.api.user.UserService;
import com.cyecize.summer.areas.validation.interfaces.ConstraintValidator;
import com.cyecize.summer.common.annotations.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UsernameTakenValidator implements ConstraintValidator<UsernameTaken, String> {

    private final UserService userService;

    @Override
    public boolean isValid(String val, Object bindingModel) {
        if (val == null) {
            return true;
        }

        return !this.userService.isUsernameOrEmailTaken(val);
    }
}
