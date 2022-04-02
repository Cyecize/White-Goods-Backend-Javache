package com.cyecize.app.api.user;

import com.cyecize.app.constants.EntityGraphs;
import com.cyecize.app.util.Specification;
import com.cyecize.app.util.SpecificationExecutor;
import com.cyecize.summer.common.annotations.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SpecificationExecutor specificationExecutor;

    @Override
    public List<String> getEmailsOfAdmins() {
        return this.userRepository.selectEmailWhereRoleEquals(RoleType.ROLE_ADMIN.name());
    }

    @Override
    public User findByUsernameOrEmail(String value) {
        final Specification<User> specification = UserSpecifications.usernameEquals(value)
                .or(UserSpecifications.emailEquals(value));
        return this.specificationExecutor.findOne(specification, User.class, EntityGraphs.USER_ALL);
    }
}
