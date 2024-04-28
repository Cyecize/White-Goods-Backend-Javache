package com.cyecize.app.api.product.selection;

import com.cyecize.app.constants.EntityGraphs;
import com.cyecize.app.util.Specification;
import com.cyecize.summer.common.annotations.Service;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@Service
@RequiredArgsConstructor
public class ProductSelectionServiceImpl implements ProductSelectionService {

    private final ProductSelectionRepository repository;
    private final ModelMapper modelMapper;

    @Override
    public List<ProductSelection> getAll(boolean enabledOnly) {
        final Specification<ProductSelection> specification = ProductSelectionSpecifications.sort()
                .and(ProductSelectionSpecifications.showOnlyEnabled(enabledOnly));

        return this.repository.findAll(specification, EntityGraphs.PRODUCT_SELECTION_FOR_SEARCH);
    }

    @Override
    public ProductSelection save(CreateProductSelectionDto dto) {
        final ProductSelection selection = this.modelMapper.map(dto, ProductSelection.class);
        this.repository.save(selection);

        return this.repository.findOne(dto.getId());
    }

    @Override
    public ProductSelection get(Long id) {
        return this.repository.findOne(id);
    }

    @Override
    public void remove(Long id) {
        this.repository.remove(id);
    }
}
