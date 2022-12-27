package com.cyecize.app.api.user;

import java.util.List;

public interface UserService {

    List<String> getEmailsOfAdmins();

    User findByUsernameOrEmail(String value);

    boolean isUsernameOrEmailTaken(String value);

    User register(UserRegisterDto dto);

    void changePassword(ChangePasswordDto dto);
}
