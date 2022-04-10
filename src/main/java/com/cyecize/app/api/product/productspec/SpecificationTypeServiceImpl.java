package com.cyecize.app.api.product.productspec;

import com.cyecize.app.util.Page;
import com.cyecize.app.util.Specification;
import com.cyecize.app.util.SpecificationExecutor;
import com.cyecize.summer.common.annotations.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpecificationTypeServiceImpl implements SpecificationTypeService {

    private final SpecificationExecutor specificationExecutor;
    private final SpecificationTypeRepository specificationTypeRepository;

    @Override
    public Page<SpecificationType> findAll(SpecificationTypeQuery query) {
        final Specification<SpecificationType> spec = SpecificationTypeSpecifications
                .categoryContains(query.getCategoryIds());

        return this.specificationExecutor.findAll(spec, query.getPage(), SpecificationType.class, null);
    }

    @Override
    public boolean specificationTypeExists(Long value) {
        return this.specificationTypeRepository.existsById(value);
    }
}
