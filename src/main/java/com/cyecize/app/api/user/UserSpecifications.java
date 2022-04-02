package com.cyecize.app.api.user;

import com.cyecize.app.util.Specification;
import org.apache.commons.lang3.StringUtils;

public final class UserSpecifications {

    public static Specification<User> usernameEquals(String value) {
        if (StringUtils.trimToNull(value) == null) {
            return Specification.not(null);
        }

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(User_.username), value);
    }

    public static Specification<User> emailEquals(String value) {
        if (StringUtils.trimToNull(value) == null) {
            return Specification.not(null);
        }

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(User_.email), value);
    }
}
