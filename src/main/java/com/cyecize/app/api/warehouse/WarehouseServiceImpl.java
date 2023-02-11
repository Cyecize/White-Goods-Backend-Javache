package com.cyecize.app.api.warehouse;

import com.cyecize.app.api.product.Product;
import com.cyecize.app.api.product.ProductRepository;
import com.cyecize.app.api.store.order.Order;
import com.cyecize.app.api.warehouse.dto.CreateQuantityUpdateDto;
import com.cyecize.app.api.warehouse.dto.CreateWarehouseDeliveryDto;
import com.cyecize.app.api.warehouse.dto.CreateWarehouseRevisionDto;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.app.util.Page;
import com.cyecize.app.util.Specification;
import com.cyecize.app.util.SpecificationExecutor;
import com.cyecize.ioc.annotations.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
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

    private final WarehouseRevisionRepository warehouseRevisionRepository;

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
    @Transactional
    public void performDelivery(CreateWarehouseDeliveryDto dto) {
        final Set<Long> prodIds = dto.getItems().stream()
                .map(CreateQuantityUpdateDto::getProductId)
                .collect(Collectors.toSet());

        final Map<Long, Product> products = this.productRepository.findAllNoFetch(prodIds).stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        final WarehouseDelivery delivery = new WarehouseDelivery();
        final LocalDateTime date = LocalDateTime.now();
        delivery.setDate(date);

        this.warehouseDeliveryRepository.persist(delivery);

        final List<Product> productsToSave = new ArrayList<>();
        final List<QuantityUpdate> quantityUpdatesToSave = new ArrayList<>();
        for (CreateQuantityUpdateDto item : dto.getItems()) {
            final QuantityUpdate quantityUpdate = this.modelMapper.map(item, QuantityUpdate.class);
            quantityUpdate.setDate(date);
            quantityUpdate.setDeliveryId(delivery.getId());

            quantityUpdatesToSave.add(quantityUpdate);
            QuantityUpdateType.updateQuantity(products.get(item.getProductId()), quantityUpdate);
            productsToSave.add(products.get(item.getProductId()));
        }

        this.quantityUpdateRepository.persistAll(quantityUpdatesToSave);
        this.productRepository.mergeAll(productsToSave);
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

    @Override
    public WarehouseDelivery findDeliveryById(Long id) {
        if (id == null) {
            return null;
        }

        return this.warehouseDeliveryRepository.find(id);
    }

    @Override
    public List<QuantityUpdate> getDeliveryItems(Long deliveryId) {
        final Specification<QuantityUpdate> specification = QuantityUpdateSpecifications
                .deliveryIdEquals(deliveryId)
                .and(QuantityUpdateSpecifications.sortByIdDesc());

        return this.specificationExecutor.findAll(
                specification,
                QuantityUpdate.class,
                null
        );
    }

    @Override
    @Transactional
    public void undoDelivery(Long deliveryId) {
        final WarehouseDelivery delivery = this.findDeliveryById(deliveryId);
        final List<QuantityUpdate> deliveryItems = this.getDeliveryItems(deliveryId);

        final Set<Long> prodIds = deliveryItems.stream()
                .map(QuantityUpdate::getProductId)
                .collect(Collectors.toSet());

        final Map<Long, Product> products = this.productRepository.findAllNoFetch(prodIds)
                .stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        final List<Product> productsToSave = new ArrayList<>();
        for (QuantityUpdate item : deliveryItems) {
            QuantityUpdateType.applyUndo(products.get(item.getProductId()), item);
            productsToSave.add(products.get(item.getProductId()));
        }

        this.productRepository.mergeAll(productsToSave);
        this.quantityUpdateRepository.removeByDeliveryId(deliveryId);
        this.warehouseDeliveryRepository.remove(delivery);
    }

    @Override
    public WarehouseRevision findRevisionById(Long revisionId) {
        if (revisionId == null) {
            return null;
        }

        return this.warehouseRevisionRepository.find(revisionId);
    }

    @Override
    public List<QuantityUpdate> getRevisionItems(Long revisionId) {
        final Specification<QuantityUpdate> specification = QuantityUpdateSpecifications
                .revisionIdEquals(revisionId)
                .and(QuantityUpdateSpecifications.sortByIdDesc());

        return this.specificationExecutor.findAll(
                specification,
                QuantityUpdate.class,
                null
        );
    }

    @Override
    @Transactional
    public void performRevision(CreateWarehouseRevisionDto dto) {
        final Set<Long> prodIds = dto.getItems().stream()
                .map(CreateQuantityUpdateDto::getProductId)
                .collect(Collectors.toSet());

        final Map<Long, Product> products = this.productRepository.findAllNoFetch(prodIds).stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        final WarehouseRevision revision = new WarehouseRevision();
        final LocalDateTime date = LocalDateTime.now();
        revision.setDate(date);

        this.warehouseRevisionRepository.persist(revision);

        final List<Product> productsToSave = new ArrayList<>();
        final List<QuantityUpdate> quantityUpdatesToSave = new ArrayList<>();
        for (CreateQuantityUpdateDto item : dto.getItems()) {
            final QuantityUpdate quantityUpdate = this.modelMapper.map(item, QuantityUpdate.class);
            quantityUpdate.setDate(date);
            quantityUpdate.setRevisionId(revision.getId());
            quantityUpdate.setUpdateType(QuantityUpdateType.REVISION_REPLACE);

            quantityUpdatesToSave.add(quantityUpdate);
            QuantityUpdateType.updateQuantity(products.get(item.getProductId()), quantityUpdate);
            productsToSave.add(products.get(item.getProductId()));
        }

        this.quantityUpdateRepository.persistAll(quantityUpdatesToSave);
        this.productRepository.mergeAll(productsToSave);
    }
}
