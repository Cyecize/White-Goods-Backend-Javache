package com.cyecize.app.api.product;

import com.cyecize.ioc.annotations.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    @Override
    public List<ProductCategory> findAllCategories() {
        return this.productCategoryRepository.findALl();
    }
}
