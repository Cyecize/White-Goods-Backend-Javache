package com.cyecize.app.api.product.productspec;

import java.util.List;

public interface ProductSpecificationService {

    List<ProductSpecification> findAllSpecifications(ProductSpecificationQuery query);

    List<ProductSpecification> findAllSpecificationsById(List<Long> ids);

    ProductSpecification createProductSpecification(CreateProductSpecificationDto dto);
}
