package com.cyecize.app.api.warehouse;

import com.cyecize.app.api.product.Product;
import com.cyecize.app.api.store.order.Order;
import com.cyecize.app.api.warehouse.dto.CreateQuantityUpdateDto;
import com.cyecize.app.api.warehouse.dto.CreateWarehouseDeliveryDto;
import com.cyecize.app.util.Page;
import java.util.List;

public interface WarehouseService {

    void updateQuantity(CreateQuantityUpdateDto dto);

    boolean updateQuantity(Order order, Long productId, Integer quantity);

    void performDelivery(CreateWarehouseDeliveryDto dto);

    void initialize(Product product);

    Page<QuantityUpdate> getQuantityUpdateHistory(Long productId, QuantityUpdateQuery query);

    WarehouseDelivery findDeliveryById(Long id);

    List<QuantityUpdate> getDeliveryItems(Long deliveryId);

    void undoDelivery(Long deliveryId);
}
