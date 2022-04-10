package com.cyecize.app.api.product.productspec;

import com.cyecize.app.util.Page;

public interface SpecificationTypeService {
    Page<SpecificationType> findAll(SpecificationTypeQuery query);

    boolean specificationTypeExists(Long value);
}
