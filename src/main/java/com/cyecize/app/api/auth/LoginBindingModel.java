package com.cyecize.app.api.auth;

import com.cyecize.app.api.auth.constraints.ValidPassword;
import com.cyecize.app.api.user.User;
import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.summer.areas.validation.annotations.ConvertedBy;
import com.cyecize.summer.areas.validation.constraints.NotEmpty;
import com.cyecize.summer.areas.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginBindingModel {
    @JsonProperty("username")
    @NotNull(message = ValidationMessages.USERNAME_NOT_FOUND)
    @ConvertedBy(UsernameOrEmailConverter.class)
    private User user;

    @ValidPassword
    @NotEmpty(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    private String password;
}
