package com.cyecize.app.api.product;

import com.cyecize.app.constants.EntityGraphs;
import com.cyecize.app.util.Specification;
import com.cyecize.app.util.SpecificationExecutor;
import com.cyecize.ioc.annotations.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final SpecificationExecutor specificationExecutor;

    @Override
    public Product findEnabledProductById(Long id) {
        final Specification<Product> specification = ProductSpecifications.idEquals(id)
                .and(ProductSpecifications.enabled(true));

        return this.specificationExecutor.findOne(specification, Product.class, EntityGraphs.PRODUCT_ALL);
    }
}
