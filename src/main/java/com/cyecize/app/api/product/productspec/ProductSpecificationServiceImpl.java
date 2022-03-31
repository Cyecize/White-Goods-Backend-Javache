package com.cyecize.app.api.product.productspec;

import com.cyecize.app.util.Specification;
import com.cyecize.app.util.SpecificationExecutor;
import com.cyecize.summer.common.annotations.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSpecificationServiceImpl implements ProductSpecificationService {

    private final SpecificationExecutor specificationExecutor;

    @Override
    public List<ProductSpecification> findAllSpecifications(ProductSpecificationQuery query) {
        final Specification<ProductSpecification> specification =
                ProductSpecificationSpecifications.specificationTypeIdIn(query.getSpecificationTypeIds());

        return this.specificationExecutor.findAll(specification, ProductSpecification.class, null);
    }
}
