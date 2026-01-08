package com.cyecize.app.api.product.productspec;

import com.cyecize.app.api.product.ProductCategory;
import com.cyecize.app.constants.EntityGraphs;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.app.util.Page;
import com.cyecize.app.util.Specification;
import com.cyecize.app.util.SpecificationExecutor;
import com.cyecize.summer.common.annotations.Service;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@Service
@RequiredArgsConstructor
public class SpecificationTypeServiceImpl implements SpecificationTypeService {

    private final SpecificationExecutor specificationExecutor;
    private final SpecificationTypeRepository specificationTypeRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<SpecificationType> findAll(SpecificationTypeQuery query) {
        final Specification<SpecificationType> spec = SpecificationTypeSpecifications
                .categoryContains(query.getCategoryIds());

        return this.specificationExecutor.findAll(spec, query.getPage(), SpecificationType.class,
                null);
    }

    @Override
    public boolean specificationTypeExists(Long id) {
        return this.specificationTypeRepository.existsById(id);
    }

    @Override
    public SpecificationType getSpecificationTypeEagerlyFetch(Long id) {
        return this.specificationExecutor.findOne(
                SpecificationTypeSpecifications.idEquals(id),
                SpecificationType.class,
                EntityGraphs.SPECIFICATION_TYPE_ALL
        );
    }

    @Override
    @Transactional
    public void addCategory(SpecificationType specificationType, ProductCategory category) {
        specificationType.getCategories().add(category);
        this.specificationTypeRepository.merge(specificationType);
    }

    @Override
    @Transactional
    public void removeCategory(SpecificationType specificationType, ProductCategory category) {
        specificationType.getCategories().removeIf(cat -> cat.getId().equals(category.getId()));
        this.specificationTypeRepository.merge(specificationType);
    }

    @Override
    public boolean existsBySpecificationType(String type) {
        return this.specificationTypeRepository.existsBySpecificationType(type);
    }

    @Override
    public SpecificationType createSpecificationType(CreateSpecificationTypeDto dto) {
        final SpecificationType specificationType = this.modelMapper.map(dto,
                SpecificationType.class);
        specificationType.setCategories(new ArrayList<>());

        specificationType.getCategories().add(dto.getCategory());
        return this.specificationTypeRepository.persist(specificationType);
    }

    @Override
    @Transactional
    public SpecificationType editSpecificationType(Long id, EditSpecificationTypeDto dto) {
        final SpecificationType specificationType = this.getSpecificationTypeEagerlyFetch(id);

        //TODO: ModelMerger
        specificationType.setTitleBg(dto.getTitleBg());
        specificationType.setTitleEn(dto.getTitleEn());

        this.specificationTypeRepository.merge(specificationType);
        return specificationType;
    }
}
