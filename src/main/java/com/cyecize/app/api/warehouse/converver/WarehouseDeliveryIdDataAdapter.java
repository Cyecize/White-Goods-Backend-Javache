package com.cyecize.app.api.warehouse.converver;

import com.cyecize.app.api.warehouse.WarehouseDelivery;
import com.cyecize.app.api.warehouse.WarehouseService;
import com.cyecize.app.error.NotFoundApiException;
import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.summer.areas.validation.interfaces.DataAdapter;
import com.cyecize.summer.common.annotations.Component;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;

@Component
@RequiredArgsConstructor
public class WarehouseDeliveryIdDataAdapter implements DataAdapter<WarehouseDelivery> {

    private final WarehouseService warehouseService;

    @Override
    public WarehouseDelivery resolve(String paramName, HttpSoletRequest httpSoletRequest) {
        final long id = NumberUtils.toLong(
                httpSoletRequest.getBodyParam(paramName),
                Integer.MIN_VALUE
        );
        final WarehouseDelivery delivery = this.warehouseService.findDeliveryById(id);

        if (delivery == null) {
            throw new NotFoundApiException(String.format("Delivery with id %d was not found!", id));
        }

        return delivery;
    }
}
