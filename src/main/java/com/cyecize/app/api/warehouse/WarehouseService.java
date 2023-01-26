package com.cyecize.app.api.warehouse;

import com.cyecize.app.api.product.Product;
import com.cyecize.app.api.warehouse.dto.CreateQuantityUpdateDto;
import com.cyecize.app.api.warehouse.dto.PerformWarehouseDeliveryDto;

public interface WarehouseService {

    void updateQuantity(CreateQuantityUpdateDto dto);

    void performDelivery(PerformWarehouseDeliveryDto dto);

    void initialize(Product product);
}
