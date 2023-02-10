package com.cyecize.app.api.warehouse.converver;

import com.cyecize.app.api.warehouse.WarehouseRevision;
import com.cyecize.app.api.warehouse.WarehouseService;
import com.cyecize.app.error.NotFoundApiException;
import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.summer.areas.validation.interfaces.DataAdapter;
import com.cyecize.summer.common.annotations.Component;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;

@Component
@RequiredArgsConstructor
public class WarehouseRevisionIdDataAdapter implements DataAdapter<WarehouseRevision> {

    private final WarehouseService warehouseService;

    @Override
    public WarehouseRevision resolve(String paramName, HttpSoletRequest httpSoletRequest) {
        final long id = NumberUtils.toLong(
                httpSoletRequest.getBodyParam(paramName),
                Integer.MIN_VALUE
        );
        final WarehouseRevision revision = this.warehouseService.findRevisionById(id);

        if (revision == null) {
            throw new NotFoundApiException(String.format("Revision with id %d was not found!", id));
        }

        return revision;
    }
}
