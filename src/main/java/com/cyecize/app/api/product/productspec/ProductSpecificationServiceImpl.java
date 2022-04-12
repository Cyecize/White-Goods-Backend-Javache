package com.cyecize.app.api.product.productspec;

import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.app.util.Specification;
import com.cyecize.app.util.SpecificationExecutor;
import com.cyecize.summer.common.annotations.Service;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSpecificationServiceImpl implements ProductSpecificationService {

    private final SpecificationExecutor specificationExecutor;
    private final ModelMapper modelMapper;
    private final ProductSpecificationRepository repository;

    @Override
    public List<ProductSpecification> findAllSpecifications(ProductSpecificationQuery query) {
        final Specification<ProductSpecification> specification =
                ProductSpecificationSpecifications.specificationTypeIdIn(query.getSpecificationTypeIds());

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
                null
        );
    }

    @Override
    @Transactional
    public ProductSpecification createProductSpecification(CreateProductSpecificationDto dto) {
        return this.repository.persist(this.modelMapper.map(dto, ProductSpecification.class));
    }
}
