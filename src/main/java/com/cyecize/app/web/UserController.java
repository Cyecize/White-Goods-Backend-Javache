package com.cyecize.app.web;

import com.cyecize.app.api.user.User;
import com.cyecize.app.api.user.UserRegisterDto;
import com.cyecize.app.api.user.UserService;
import com.cyecize.app.constants.Endpoints;
import com.cyecize.summer.areas.security.annotations.PreAuthorize;
import com.cyecize.summer.areas.security.enums.AuthorizationType;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.PostMapping;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Controller
@PreAuthorize(AuthorizationType.LOGGED_IN)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(Endpoints.REGISTER)
    @PreAuthorize(AuthorizationType.ANONYMOUS)
    public UserRegisteredResponseDto register(@Valid UserRegisterDto dto) {
        final User user = this.userService.register(dto);
        return new UserRegisteredResponseDto(user.getUsername());
    }

    @Data
    static class UserRegisteredResponseDto {

        private final String username;
    }
}
