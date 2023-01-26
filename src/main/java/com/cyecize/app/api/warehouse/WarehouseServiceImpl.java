package com.cyecize.app.api.warehouse;

import com.cyecize.app.api.product.Product;
import com.cyecize.app.api.warehouse.dto.CreateQuantityUpdateDto;
import com.cyecize.app.api.warehouse.dto.PerformWarehouseDeliveryDto;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.ioc.annotations.Service;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseDeliveryRepository warehouseDeliveryRepository;
    private final QuantityUpdateRepository quantityUpdateRepository;

    @Override
    public void updateQuantity(CreateQuantityUpdateDto dto) {

    }

    @Override
    public void performDelivery(PerformWarehouseDeliveryDto dto) {

    }

    @Override
    @Transactional
    public void initialize(Product product) {
        final QuantityUpdate quantityUpdate = new QuantityUpdate();
        quantityUpdate.setQuantityValue(product.getQuantity());
        quantityUpdate.setUpdateType(QuantityUpdateType.INITIAL_REPLACE);
        quantityUpdate.setProductId(product.getId());
        quantityUpdate.setDate(LocalDateTime.now());

        this.quantityUpdateRepository.persist(quantityUpdate);
    }
}
