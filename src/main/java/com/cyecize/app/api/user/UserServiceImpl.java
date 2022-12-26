package com.cyecize.app.api.user;

import com.cyecize.app.constants.EntityGraphs;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.app.util.Specification;
import com.cyecize.app.util.SpecificationExecutor;
import com.cyecize.summer.common.annotations.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;

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

    @Override
    public boolean isUsernameOrEmailTaken(String value) {
        if (StringUtils.trimToNull(value) == null) {
            return false;
        }

        final Specification<User> specification = UserSpecifications.usernameEquals(value)
                .or(UserSpecifications.emailEquals(value));
        return this.specificationExecutor.exists(specification, User.class);
    }

    @Override
    @Transactional
    public User register(UserRegisterDto dto) {
        final User user = new User();

        user.setDateRegistered(LocalDateTime.now());
        user.setEmail(dto.getEmail());
        user.setPassword(BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt()));

        user.setUsername(Objects.requireNonNullElse(
                StringUtils.trimToNull(dto.getUsername()),
                this.generateUniqueUsername(dto.getEmail())
        ));

        user.setRoles(new ArrayList<>());
        return this.userRepository.persist(user);
    }

    private String generateUniqueUsername(String email) {
        final String usrName = email.split("@")[0];
        if (!this.isUsernameOrEmailTaken(usrName)) {
            return usrName;
        }

        int count = 1;
        String generatedName;
        do {
            generatedName = usrName + "_" + count;
            count++;
        } while (this.isUsernameOrEmailTaken(generatedName));

        return generatedName;
    }
}
