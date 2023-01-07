package com.cyecize.app.web;

import com.cyecize.app.api.auth.AuthTokenDto;
import com.cyecize.app.api.auth.AuthenticationService;
import com.cyecize.app.api.auth.ForgottenPasswordDto;
import com.cyecize.app.api.auth.LoginBindingModel;
import com.cyecize.app.api.auth.ResetPasswordDto;
import com.cyecize.app.api.user.UserDto;
import com.cyecize.app.api.user.UserService;
import com.cyecize.app.constants.Endpoints;
import com.cyecize.app.constants.General;
import com.cyecize.http.HttpStatus;
import com.cyecize.summer.areas.security.annotations.PreAuthorize;
import com.cyecize.summer.areas.security.enums.AuthorizationType;
import com.cyecize.summer.areas.security.models.Principal;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.GetMapping;
import com.cyecize.summer.common.annotations.routing.PostMapping;
import com.cyecize.summer.common.annotations.routing.RequestMapping;
import com.cyecize.summer.common.models.JsonResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "", produces = General.APPLICATION_JSON)
public class SecurityController {

    private final ModelMapper modelMapper;

    private final AuthenticationService authenticationService;

    private final UserService userService;

    @PostMapping(Endpoints.LOGIN)
    @PreAuthorize(AuthorizationType.ANONYMOUS)
    public AuthTokenDto login(@Valid LoginBindingModel loginBindingModel) {
        return this.modelMapper.map(this.authenticationService.login(loginBindingModel),
                AuthTokenDto.class);
    }

    @GetMapping(Endpoints.USER_DETAILS)
    @PreAuthorize(value = AuthorizationType.LOGGED_IN)
    public UserDto getUserDetails(Principal principal) {
        return this.modelMapper.map(principal.getUser(), UserDto.class);
    }

    @PostMapping(Endpoints.LOGOUT)
    public void logout() {
        throw new RuntimeException("This should not be reached!");
    }

    @PostMapping(Endpoints.RECOVERY_EMAIL)
    @PreAuthorize(AuthorizationType.ANONYMOUS)
    public JsonResponse sendRecoveryEmail(ForgottenPasswordDto dto) {
        if (dto.getUser() != null) {
            this.authenticationService.sendRecoveryEmail(dto.getUser());
        }
        return new JsonResponse(HttpStatus.OK)
                .addAttribute("message", "recovery.email.sent");
    }

    @PostMapping(Endpoints.PASSWORD_RESET)
    @PreAuthorize(AuthorizationType.ANONYMOUS)
    public JsonResponse resetPassword(@Valid ResetPasswordDto dto) {
        this.userService.changePassword(dto);
        return new JsonResponse(HttpStatus.OK)
                .addAttribute("message", "Password reset!");
    }
}
