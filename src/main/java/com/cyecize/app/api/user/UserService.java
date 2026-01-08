package com.cyecize.app.api.user;

import com.cyecize.app.api.auth.ResetPasswordDto;
import java.util.List;

public interface UserService {

    List<String> getEmailsOfAdmins();

    User findByUsernameOrEmail(String value);

    boolean isUsernameOrEmailTaken(String value);

    User register(UserRegisterDto dto);

    void changePassword(ChangePasswordDto dto);

    void changePassword(ResetPasswordDto dto);

    void deleteUser(User user);
}
