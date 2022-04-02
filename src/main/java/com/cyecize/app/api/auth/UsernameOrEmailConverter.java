package com.cyecize.app.api.auth;

import com.cyecize.app.api.user.User;
import com.cyecize.app.api.user.UserService;
import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.summer.areas.validation.interfaces.DataAdapter;
import com.cyecize.summer.common.annotations.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UsernameOrEmailConverter implements DataAdapter<User> {
    private final UserService userService;

    @Override
    public User resolve(String param, HttpSoletRequest request) {
        if (param == null) {
            return null;
        }

        return this.userService.findByUsernameOrEmail(param);
    }
}
