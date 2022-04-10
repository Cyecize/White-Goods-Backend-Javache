package com.cyecize.app.api.product;

import com.cyecize.app.api.base64.Base64FileService;
import com.cyecize.app.api.product.dto.CreateCategoryDto;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.ioc.annotations.Service;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;
    private final ModelMapper modelMapper;
    private final Base64FileService base64FileService;
    private final TagService tagService;

    @Override
    public List<ProductCategory> findAllCategories() {
        return this.productCategoryRepository.findAll();
    }

    @Override
    @Transactional
    public ProductCategory createCategory(CreateCategoryDto dto) {
        final ProductCategory productCategory = this.modelMapper.map(dto, ProductCategory.class);
        productCategory.setImageUrl(this.base64FileService.saveFile(dto.getImage()));
        productCategory.setTags(this.tagService.findOrCreate(dto.getTagNames()));
        return this.productCategoryRepository.persist(productCategory);
    }

    @Override
    public ProductCategory getCategory(Long categoryId) {
        return this.productCategoryRepository.find(categoryId);
    }
}
