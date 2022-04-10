package com.cyecize.app.api.product;

import com.cyecize.app.api.product.dto.CreateCategoryDto;

import java.util.List;

public interface ProductCategoryService {

    List<ProductCategory> findAllCategories();

    ProductCategory createCategory(CreateCategoryDto dto);

    ProductCategory getCategory(Long categoryId);
}
