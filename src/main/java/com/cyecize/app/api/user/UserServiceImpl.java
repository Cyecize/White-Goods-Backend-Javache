package com.cyecize.app.api.user;

import com.cyecize.summer.common.annotations.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<String> getEmailsOfAdmins() {
        return this.userRepository.selectEmailWhereRoleEquals(RoleType.ROLE_ADMIN.name());
    }
}
