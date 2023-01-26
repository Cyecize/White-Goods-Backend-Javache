package com.cyecize.app.api.warehouse;

import com.cyecize.app.api.product.Product;
import com.cyecize.app.api.product.ProductRepository;
import com.cyecize.app.api.warehouse.dto.CreateQuantityUpdateDto;
import com.cyecize.app.api.warehouse.dto.PerformWarehouseDeliveryDto;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.ioc.annotations.Service;
import com.cyecize.summer.areas.security.models.Principal;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseDeliveryRepository warehouseDeliveryRepository;
    private final QuantityUpdateRepository quantityUpdateRepository;

    private final ProductRepository productRepository;

    private final Principal principal;

    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void updateQuantity(CreateQuantityUpdateDto dto) {
        final Product product = this.productRepository.find(dto.getProductId());

        final QuantityUpdate quantityUpdate = this.modelMapper.map(dto, QuantityUpdate.class);
        quantityUpdate.setDate(LocalDateTime.now());

        this.quantityUpdateRepository.persist(quantityUpdate);
        QuantityUpdateType.updateQuantity(product, quantityUpdate);
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
