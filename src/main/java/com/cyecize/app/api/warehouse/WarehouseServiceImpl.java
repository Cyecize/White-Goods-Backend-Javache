package com.cyecize.app.api.warehouse;

import com.cyecize.app.api.product.Product;
import com.cyecize.app.api.product.ProductRepository;
import com.cyecize.app.api.store.order.Order;
import com.cyecize.app.api.warehouse.dto.CreateQuantityUpdateDto;
import com.cyecize.app.api.warehouse.dto.PerformWarehouseDeliveryDto;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.app.util.Page;
import com.cyecize.app.util.Specification;
import com.cyecize.app.util.SpecificationExecutor;
import com.cyecize.ioc.annotations.Service;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseDeliveryRepository warehouseDeliveryRepository;

    private final QuantityUpdateRepository quantityUpdateRepository;

    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;

    private final SpecificationExecutor specificationExecutor;

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
    public boolean updateQuantity(Order order, Long productId, Integer quantity) {
        final QuantityUpdate quantityUpdate = new QuantityUpdate();
        quantityUpdate.setDate(LocalDateTime.now());
        quantityUpdate.setQuantityValue(quantity);
        quantityUpdate.setUpdateType(QuantityUpdateType.ORDER_DECREASE);
        quantityUpdate.setProductId(productId);
        quantityUpdate.setOrderId(order.getId());

        final boolean updated = this.productRepository.subtractQuantity(productId, quantity);

        if (updated) {
            this.quantityUpdateRepository.persist(quantityUpdate);
        }

        return updated;
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

    @Override
    @Transactional
    public Page<QuantityUpdate> getQuantityUpdateHistory(Long productId,
            QuantityUpdateQuery query) {
        final Specification<QuantityUpdate> specification = QuantityUpdateSpecifications
                .productIdEquals(productId)
                .and(QuantityUpdateSpecifications.sortByIdDesc());

        return this.specificationExecutor.findAll(
                specification,
                query.getPage(),
                QuantityUpdate.class,
                null
        );
    }
}
