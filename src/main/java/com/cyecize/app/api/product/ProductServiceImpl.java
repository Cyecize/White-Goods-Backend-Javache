package com.cyecize.app.api.product;

import com.cyecize.app.api.base64.Base64FileService;
import com.cyecize.app.api.product.dto.CreateProductDto;
import com.cyecize.app.api.product.dto.EditProductDto;
import com.cyecize.app.api.product.productspec.CreateProductSpecificationDto;
import com.cyecize.app.api.product.productspec.ProductSpecification;
import com.cyecize.app.api.product.productspec.ProductSpecificationService;
import com.cyecize.app.api.user.User;
import com.cyecize.app.constants.EntityGraphs;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.app.util.Page;
import com.cyecize.app.util.Specification;
import com.cyecize.app.util.SpecificationExecutor;
import com.cyecize.ioc.annotations.Service;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final SpecificationExecutor specificationExecutor;
    private final ModelMapper modelMapper;
    private final TagService tagService;
    private final Base64FileService base64FileService;
    private final ProductRepository repository;
    private final ProductSpecificationService productSpecificationService;
    private final ImageService imageService;

    @Override
    public boolean existsById(Long id) {
        return this.repository.existsById(id);
    }

    @Override
    public boolean existsByIdAndMeetsQuantity(Long id, Integer quantity) {
        return this.repository.existsByIdAndQuantityGreaterOrEqual(id, quantity);
    }

    @Override
    public boolean subtractQuantity(Long productId, Integer quantity) {
        return this.repository.subtractQuantity(productId, quantity);
    }

    @Override
    public Product findProductById(Long id, User currentUser) {
        final Specification<Product> specification = ProductSpecifications.idEquals(id)
                .and(ProductSpecifications.showHidden(true, currentUser));

        return this.specificationExecutor.findOne(specification, Product.class, EntityGraphs.PRODUCT_ALL);
    }

    @Override
    public Page<Product> searchProducts(ProductQuery productQuery, User currentUser) {
        final Specification<Product> specification = ProductSpecifications
                .showHidden(productQuery.getShowHidden(), currentUser)
                .and(ProductSpecifications.sort(productQuery.getSort()))
                .and(ProductSpecifications.categoryIdContains(productQuery.getCategoryIds()))
                .and(ProductSpecifications.includesAllSpecifications(productQuery.getSpecifications()))
                .and(ProductSpecifications.containsText(productQuery.getSearch()));

        return this.specificationExecutor.findAll(
                specification, productQuery.getPage(), Product.class, EntityGraphs.PRODUCT_FOR_SEARCH
        );
    }

    @Override
    public List<Product> findAllByIdIn(Collection<Long> ids) {
        final Specification<Product> specification = ProductSpecifications
                .showHidden(false, null)
                .and(ProductSpecifications.idContains(ids));

        return this.specificationExecutor.findAll(
                specification,
                Product.class,
                EntityGraphs.PRODUCT_FOR_SEARCH
        );
    }

    @Override
    @Transactional
    public Product createProduct(CreateProductDto createProductDto) {
        final Product product = this.modelMapper.map(createProductDto, Product.class);

        product.setCategoryId(createProductDto.getCategory().getId());
        product.setTags(this.tagService.findOrCreate(createProductDto.getTagNames()));
        product.setImageUrl(this.base64FileService.saveFile(createProductDto.getImage()));

        final Set<ProductSpecification> specifications = new HashSet<>();
        if (createProductDto.getProductSpecifications() != null) {
            for (CreateProductSpecificationDto dto : createProductDto.getProductSpecifications()) {
                specifications.add(this.productSpecificationService.createProductSpecification(dto));
            }
        }

        if (createProductDto.getExistingProductSpecifications() != null) {
            specifications.addAll(createProductDto.getExistingProductSpecifications());
        }

        product.setSpecifications(specifications);

        this.repository.persist(product);

        final Set<Image> images = this.imageService.createImages(createProductDto.getGallery());
        if (!images.isEmpty()) {
            images.forEach(image -> image.setProductId(product.getId()));
            product.setImages(images);
            this.imageService.persistAll(images);
        }

        return product;
    }

    @Override
    @Transactional
    public Product editProduct(Long id, EditProductDto dto) {
        final Product product = this.specificationExecutor.findOne(
                ProductSpecifications.idEquals(id), Product.class, EntityGraphs.PRODUCT_ALL
        );

        //TODO: ModelMerger
        product.setCategoryId(dto.getCategory().getId());
        product.setCategory(dto.getCategory());
        product.setEnabled(dto.getEnabled());
        product.setProductName(dto.getProductName());
        product.setDescriptionBg(dto.getDescriptionBg());
        product.setDescriptionEn(dto.getDescriptionEn());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());

        product.setTags(this.tagService.findOrCreate(dto.getTagNames()));

        if (dto.getImage() != null) {
            this.base64FileService.removeFile(product.getImageUrl());
            product.setImageUrl(this.base64FileService.saveFile(dto.getImage()));
        }

        final Set<ProductSpecification> specifications = new HashSet<>();
        if (dto.getProductSpecifications() != null) {
            for (CreateProductSpecificationDto specDto : dto.getProductSpecifications()) {
                specifications.add(this.productSpecificationService.createProductSpecification(specDto));
            }
        }

        if (dto.getExistingProductSpecifications() != null) {
            specifications.addAll(dto.getExistingProductSpecifications());
        }

        product.getSpecifications().clear();
        product.getSpecifications().addAll(specifications);

        final Set<Image> images = this.imageService.createImages(dto.getGallery());
        if (!images.isEmpty()) {
            images.forEach(image -> image.setProductId(product.getId()));
            this.imageService.persistAll(images);
            product.getImages().addAll(images);
        }

        this.repository.merge(product);

        product.setSpecifications(new HashSet<>(specifications));

        return product;
    }
}
