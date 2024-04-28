package com.cyecize.app.api.product.selection;

import com.cyecize.app.api.product.Product_;
import com.cyecize.app.util.QuerySpecificationUtils;
import com.cyecize.app.util.QuerySpecifications;
import com.cyecize.app.util.SortDirection;
import com.cyecize.app.util.Specification;

public class ProductSelectionSpecifications {

    public static Specification<ProductSelection> showOnlyEnabled(boolean showOnlyEnabled) {
        if (!showOnlyEnabled) {
            return Specification.where(null);
        }

        return (root, query, criteriaBuilder) -> QuerySpecifications.equal(
                QuerySpecificationUtils.getOrCreateJoin(
                        root, ProductSelection_.product).get(Product_.enabled),
                true,
                criteriaBuilder
        );
    }

    public static Specification<ProductSelection> sort() {
        return (root, query, criteriaBuilder) -> QuerySpecifications.sort(
                ProductSelection.class,
                root.get(ProductSelection_.orderNumber),
                SortDirection.ASC
        ).toPredicate(root, query, criteriaBuilder);
    }
}
