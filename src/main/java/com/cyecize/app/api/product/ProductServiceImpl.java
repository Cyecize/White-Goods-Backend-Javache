package com.cyecize.app.api.product;

import com.cyecize.app.constants.EntityGraphs;
import com.cyecize.app.util.Page;
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

    @Override
    public Page<Product> searchProducts(ProductQuery productQuery) {
        //TODO: maybe filter enabled based on logged user and permissions
        final Specification<Product> specification = ProductSpecifications.enabled(true)
                .and(ProductSpecifications.sort(productQuery.getSort()))
                .and(ProductSpecifications.categoryIdContains(productQuery.getCategoryIds()))
                .and(ProductSpecifications.containsText(productQuery.getSearch()));

        return this.specificationExecutor.findAll(
                specification, productQuery.getPage(), Product.class, EntityGraphs.PRODUCT_FOR_SEARCH
        );
    }
}
