package com.cyecize.app.api.auth.constraints;

import com.cyecize.app.api.user.User;

public interface ValidPasswordDto {

    User getUser();

    String getPassword();
}
