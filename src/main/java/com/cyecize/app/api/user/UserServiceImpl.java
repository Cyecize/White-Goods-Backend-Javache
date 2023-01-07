package com.cyecize.app.api.user;

import com.cyecize.app.api.auth.ResetPasswordDto;
import com.cyecize.app.api.auth.recoverykey.RecoveryKeyService;
import com.cyecize.app.constants.EntityGraphs;
import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.app.error.ApiException;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.app.util.Specification;
import com.cyecize.app.util.SpecificationExecutor;
import com.cyecize.summer.common.annotations.PostConstruct;
import com.cyecize.summer.common.annotations.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SpecificationExecutor specificationExecutor;

    private final RoleRepository roleRepository;

    private final RecoveryKeyService recoveryKeyService;

    private Map<RoleType, Role> allRoles;

    @PostConstruct
    public void init() {
        this.allRoles = new HashMap<>();
        final List<Role> roles = this.roleRepository.findAll();
        for (Role role : roles) {
            allRoles.put(role.getRole(), role);
        }

        this.allRoles = Collections.unmodifiableMap(this.allRoles);
    }

    @Override
    public List<String> getEmailsOfAdmins() {
        return this.userRepository.selectEmailWhereRoleEquals(RoleType.ROLE_ADMIN);
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
        user.getRoles().add(this.allRoles.get(RoleType.ROLE_USER));
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

    @Override
    @Transactional
    public void changePassword(ChangePasswordDto dto) {
        final User user = this.userRepository.findById(dto.getUser().getId());
        user.setPassword(BCrypt.hashpw(dto.getNewPassword(), BCrypt.gensalt()));
        this.userRepository.merge(user);
    }

    @Override
    @Transactional
    public void changePassword(ResetPasswordDto dto) {
        final User user = this.userRepository.findById(dto.getRecoveryKey().getUserId());
        user.setPassword(BCrypt.hashpw(dto.getNewPassword(), BCrypt.gensalt()));
        this.userRepository.merge(user);
        this.recoveryKeyService.destroyKeys(user.getId());
    }

    @Override
    @Transactional
    public void deleteUser(User user) {
        final boolean canDeleteAccount = user.getRoles().stream()
                .noneMatch(role -> role.getRole().isForbidUserDelete());

        if (!canDeleteAccount) {
            throw new ApiException(ValidationMessages.CANNOT_DELETE_ADMIN);
        }

        this.userRepository.deleteById(user.getId());
    }
}
