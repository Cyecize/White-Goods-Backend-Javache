package com.cyecize.app.api.product;

import com.cyecize.app.api.base64.Base64FileService;
import com.cyecize.app.api.product.dto.CreateCategoryDto;
import com.cyecize.app.constants.EntityGraphs;
import com.cyecize.app.error.NotFoundApiException;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.app.util.SpecificationExecutor;
import com.cyecize.ioc.annotations.Service;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@Service
@RequiredArgsConstructor
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;
    private final ModelMapper modelMapper;
    private final Base64FileService base64FileService;
    private final TagService tagService;
    private final SpecificationExecutor specificationExecutor;

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
        return this.specificationExecutor.findOne(
                (root, query, cb) -> cb.equal(root.get(ProductCategory_.id), categoryId),
                ProductCategory.class,
                EntityGraphs.PRODUCT_CATEGORY_ALL
        );
    }

    @Override
    @Transactional
    public ProductCategory editCategory(Long categoryId, CreateCategoryDto dto) {
        final ProductCategory productCategory = this.productCategoryRepository.find(categoryId);
        if (productCategory == null) {
            throw new NotFoundApiException(String.format(
                    "Category with id %d was not found!", categoryId
            ));
        }

        //TODO: User model merger
        productCategory.setNameBg(dto.getNameBg());
        productCategory.setNameEn(dto.getNameEn());

        this.base64FileService.removeFile(productCategory.getImageUrl());
        productCategory.setImageUrl(this.base64FileService.saveFile(dto.getImage()));
        productCategory.setTags(this.tagService.findOrCreate(dto.getTagNames()));

        this.productCategoryRepository.merge(productCategory);
        return productCategory;
    }

    @Override
    public void removeCategory(Long categoryId) {
        final ProductCategory productCategory = this.productCategoryRepository.find(categoryId);
        if (productCategory == null) {
            throw new NotFoundApiException(String.format(
                    "Category with id %d was not found!", categoryId
            ));
        }

        this.productCategoryRepository.deleteById(categoryId);
    }
}
