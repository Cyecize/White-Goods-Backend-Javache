package com.cyecize.app.api.product;

import com.cyecize.app.api.base64.Base64FileBindingModel;
import com.cyecize.app.api.base64.Base64FileService;
import com.cyecize.app.api.product.dto.CreateProductDto;
import com.cyecize.app.api.product.productspec.CreateProductSpecificationDto;
import com.cyecize.app.api.product.productspec.ProductSpecification;
import com.cyecize.app.api.product.productspec.ProductSpecificationService;
import com.cyecize.app.constants.EntityGraphs;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.app.util.Page;
import com.cyecize.app.util.Specification;
import com.cyecize.app.util.SpecificationExecutor;
import com.cyecize.ioc.annotations.Service;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.Collection;
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
    //TODO: Add service
    private final ImageRepository imageRepository;

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
                .and(ProductSpecifications.includesAllSpecifications(productQuery.getSpecifications()))
                .and(ProductSpecifications.containsText(productQuery.getSearch()));

        return this.specificationExecutor.findAll(
                specification, productQuery.getPage(), Product.class, EntityGraphs.PRODUCT_FOR_SEARCH
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

        final Set<Image> images = this.createImages(createProductDto.getGallery());
        if (!images.isEmpty()) {
            images.forEach(image -> image.setProductId(product.getId()));
            product.setImages(images);
            this.imageRepository.persistAll(images);
        }

        return product;
    }

    //TODO: Move to its own service
    private Set<Image> createImages(Collection<Base64FileBindingModel> imageDtos) {
        final Set<Image> images = new HashSet<>();
        if (imageDtos == null) {
            return images;
        }

        for (Base64FileBindingModel imageDto : imageDtos) {
            final Image image = new Image();
            image.setImageUrl(this.base64FileService.saveFile(imageDto));
            images.add(image);
        }

        return images;
    }
}
