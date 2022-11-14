package com.cyecize.app.api.product;

import com.cyecize.app.api.product.dto.CreateProductDto;
import com.cyecize.app.api.product.dto.EditProductDto;
import com.cyecize.app.api.user.User;
import com.cyecize.app.util.Page;
import java.util.Collection;
import java.util.List;

public interface ProductService {

    boolean existsById(Long id);

    Product findProductById(Long id, User currentUser);

    Page<Product> searchProducts(ProductQuery productQuery, User currentUser);

    List<Product> findAllByIdIn(Collection<Long> ids);

    Product createProduct(CreateProductDto createProductDto);

    Product editProduct(Long id, EditProductDto dto);
}
