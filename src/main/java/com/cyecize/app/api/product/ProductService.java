package com.cyecize.app.api.product;

import com.cyecize.app.util.Page;

public interface ProductService {

    Product findEnabledProductById(Long id);

    Page<Product> searchProducts(ProductQuery productQuery);
}
