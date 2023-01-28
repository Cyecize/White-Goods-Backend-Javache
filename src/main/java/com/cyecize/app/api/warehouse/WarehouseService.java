package com.cyecize.app.api.warehouse;

import com.cyecize.app.api.product.Product;
import com.cyecize.app.api.warehouse.dto.CreateQuantityUpdateDto;
import com.cyecize.app.api.warehouse.dto.PerformWarehouseDeliveryDto;
import com.cyecize.app.util.Page;

public interface WarehouseService {

    void updateQuantity(CreateQuantityUpdateDto dto);

    void performDelivery(PerformWarehouseDeliveryDto dto);

    void initialize(Product product);

    Page<QuantityUpdate> getQuantityUpdateHistory(Long productId, QuantityUpdateQuery query);
}
