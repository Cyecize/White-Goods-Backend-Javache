package com.cyecize.app.api.product.productspec;

import java.util.List;

public interface ProductSpecificationService {

    List<ProductSpecification> findAllSpecifications(ProductSpecificationQuery query);
}
