package com.cyecize.app.api.auth;

import com.cyecize.app.api.user.User;
import com.cyecize.summer.areas.validation.annotations.ConvertedBy;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ForgottenPasswordDto {

    @JsonProperty("username")
    @ConvertedBy(UsernameOrEmailConverter.class)
    private User user;
}
