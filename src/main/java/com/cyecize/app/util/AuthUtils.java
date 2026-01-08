package com.cyecize.app.util;

import com.cyecize.app.api.user.RoleType;
import com.cyecize.app.api.user.User;

public final class AuthUtils {

    public static boolean hasAdminRole(User user) {
        if (user == null) {
            return false;
        }

        return user.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals(RoleType.ROLE_ADMIN.name()));
    }
}
