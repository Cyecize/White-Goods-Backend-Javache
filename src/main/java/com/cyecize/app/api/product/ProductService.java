package com.cyecize.app.api.product;

import com.cyecize.app.api.product.dto.CreateProductDto;
import com.cyecize.app.api.user.User;
import com.cyecize.app.util.Page;

public interface ProductService {

    Product findProductById(Long id, User currentUser);

    Page<Product> searchProducts(ProductQuery productQuery, User currentUser);

    Product createProduct(CreateProductDto createProductDto);
}
