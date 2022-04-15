package com.cyecize.app.api.product.productspec;

import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.summer.areas.validation.interfaces.DataAdapter;
import com.cyecize.summer.common.annotations.Component;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;

@Component
@RequiredArgsConstructor
public class SpecificationTypeIdDataAdapter implements DataAdapter<SpecificationType> {
    private final SpecificationTypeService specificationTypeService;

    @Override
    public SpecificationType resolve(String paramName, HttpSoletRequest request) {
        long id = NumberUtils.toLong(paramName, Long.MIN_VALUE);
        if (id == Long.MIN_VALUE) {
            id = NumberUtils.toLong(request.getBodyParam(paramName), Long.MIN_VALUE);
        }

        if (id == Long.MIN_VALUE) {
            return null;
        }

        return this.specificationTypeService.getSpecificationTypeEagerlyFetch(id);
    }
}
