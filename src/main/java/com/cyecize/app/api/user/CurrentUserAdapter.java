package com.cyecize.app.api.user;

import com.cyecize.app.error.ApiException;
import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.summer.areas.security.models.Principal;
import com.cyecize.summer.areas.validation.interfaces.DataAdapter;
import com.cyecize.summer.common.annotations.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CurrentUserAdapter implements DataAdapter<User> {

    private final Principal principal;

    @Override
    public User resolve(String val, HttpSoletRequest httpSoletRequest) {
        if (this.principal.getUser() == null) {
            throw new ApiException("You must be logged in!");
        }
        return (User) this.principal.getUser();
    }
}
