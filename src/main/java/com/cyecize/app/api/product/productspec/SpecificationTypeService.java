package com.cyecize.app.api.product.productspec;

import com.cyecize.app.api.product.ProductCategory;
import com.cyecize.app.util.Page;

public interface SpecificationTypeService {
    Page<SpecificationType> findAll(SpecificationTypeQuery query);

    boolean specificationTypeExists(Long id);

    SpecificationType getSpecificationTypeEagerlyFetch(Long id);

    void addCategory(SpecificationType specificationType, ProductCategory category);

    void removeCategory(SpecificationType specificationType, ProductCategory category);

    boolean existsBySpecificationType(String type);

    SpecificationType createSpecificationType(CreateSpecificationTypeDto dto);

    SpecificationType editSpecificationType(Long id, EditSpecificationTypeDto dto);
}
