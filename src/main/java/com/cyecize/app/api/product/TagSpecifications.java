package com.cyecize.app.api.product;

import com.cyecize.app.util.Specification;

import java.util.List;

public final class TagSpecifications {
    public static Specification<Tag> tagNamesContains(List<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) {
            return Specification.not(null);
        }

        return (root, query, criteriaBuilder) -> root.get(Tag_.name).in(tagNames);
    }
}
