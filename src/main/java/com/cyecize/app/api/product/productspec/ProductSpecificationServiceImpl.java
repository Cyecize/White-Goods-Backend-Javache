package com.cyecize.app.api.product.productspec;

import static com.cyecize.app.api.product.productspec.ProductSpecificationSpecifications.assignedToProduct;
import static com.cyecize.app.api.product.productspec.ProductSpecificationSpecifications.specificationTypeIdIn;

import com.cyecize.app.constants.EntityGraphs;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.app.util.SpecificationExecutor;
import com.cyecize.summer.common.annotations.Service;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@Service
@RequiredArgsConstructor
public class ProductSpecificationServiceImpl implements ProductSpecificationService {

    private final SpecificationExecutor specificationExecutor;
    private final ModelMapper modelMapper;
    private final ProductSpecificationRepository repository;

    @Override
    public List<ProductSpecification> findAllSpecifications(ProductSpecificationQuery query) {
        var specification = specificationTypeIdIn(query.getSpecificationTypeIds());

        if (query.getOnlyAssignedValues()) {
            specification = assignedToProduct();
        }

        return this.specificationExecutor.findAll(specification, ProductSpecification.class, null);
    }

    @Override
    public List<ProductSpecification> findAllSpecificationsById(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }

        return this.specificationExecutor.findAll(
                ProductSpecificationSpecifications.idIn(ids),
                ProductSpecification.class,
                EntityGraphs.PRODUCT_SPECIFICATIONS_ALL
        );
    }

    @Override
    @Transactional
    public ProductSpecification createProductSpecification(CreateProductSpecificationDto dto) {
        return this.repository.persist(this.modelMapper.map(dto, ProductSpecification.class));
    }

    @Override
    public ProductSpecification findById(Long id) {
        return this.specificationExecutor.findOne(
                ProductSpecificationSpecifications.idIn(List.of(id)),
                ProductSpecification.class,
                null
        );
    }

    @Override
    @Transactional
    public ProductSpecification editProductSpecification(Long id, EditProductSpecificationDto dto) {
        final ProductSpecification specification = this.findById(id);

        //TODO: ModelMerger
        specification.setValueBg(dto.getValueBg());
        specification.setValueEn(dto.getValueEn());

        this.repository.merge(specification);
        return specification;
    }
}
