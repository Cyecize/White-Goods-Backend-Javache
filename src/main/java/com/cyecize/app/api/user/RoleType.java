package com.cyecize.app.api.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum RoleType {
    ROLE_USER(false),
    ROLE_ADMIN(true),
    ROLE_GOD(true);

    @Getter
    private final boolean forbidUserDelete;
}
