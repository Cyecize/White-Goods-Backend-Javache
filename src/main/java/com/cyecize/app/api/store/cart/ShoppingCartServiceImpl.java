package com.cyecize.app.api.store.cart;

import static com.cyecize.app.constants.ValidationMessages.INVALID_SHOPPING_CART_SESSION;

import com.cyecize.app.api.product.ProductService;
import com.cyecize.app.api.product.dto.ProductDto;
import com.cyecize.app.api.user.User;
import com.cyecize.app.constants.General;
import com.cyecize.app.error.ApiException;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.areas.security.models.Principal;
import com.cyecize.summer.common.annotations.Configuration;
import com.cyecize.summer.common.annotations.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final Map<String, ShoppingCartDto> sessionItems = new ConcurrentHashMap<>();

    private final Principal principal;

    private final ProductService productService;

    private final ModelMapper modelMapper;

    private final ShoppingCartRepository shoppingCartRepository;

    private final ShoppingCartItemRepository shoppingCartItemRepository;

    @Configuration("shopping.cart.session.lifetime.hours")
    private final int sessionLifetimeHours;

    @Override
    public void removeExpiredSessions() {
        final LocalDateTime min = LocalDateTime.now().minusHours(this.sessionLifetimeHours);
        final List<String> keysToRemove = this.sessionItems.entrySet().stream()
                .filter(entry -> entry.getValue().getLastModified().isBefore(min))
                .map(Entry::getKey)
                .collect(Collectors.toList());

        keysToRemove.forEach(this.sessionItems::remove);
    }

    @Override
    public String createSession() {
        final String sessionId;
        while (true) {
            final String uuid = UUID.randomUUID().toString();
            if (!this.sessionItems.containsKey(uuid)) {
                sessionId = uuid;
                break;
            }
        }

        final ShoppingCartDto cart = new ShoppingCartDto();
        cart.setLastModified(LocalDateTime.now());
        cart.setItems(new ArrayList<>());

        this.sessionItems.put(sessionId, cart);

        if (this.principal.isUserPresent()) {
            this.mergeIntoSession(sessionId);
        }

        return sessionId;
    }

    @Override
    public void removeAllItems(String sessionId) {
        if (this.principal.isUserPresent()) {
            this.shoppingCartRepository.deleteByUserId(this.getUser().getId());
        }
        this.sessionItems.remove(sessionId);
    }

    @Override
    public List<ShoppingCartItemDetailedDto> getShoppingCartItems(String sessionId, boolean merge) {
        if (this.principal.isUserPresent() && merge) {
            return this.fetchItems(this.mergeIntoSession(sessionId));
        }

        return this.fetchItems(this.getShoppingCartFromSession(sessionId).getItems());
    }

    @Override
    @Transactional
    public List<ShoppingCartItemDetailedDto> addItem(String sessionId, AddShoppingCartItemDto dto) {
        if (this.principal.isUserPresent()) {
            this.mergeIntoSession(sessionId);
        }

        final ShoppingCartDto shoppingCartFromSession = this.getShoppingCartFromSession(sessionId);
        final ShoppingCartItemDto item = new ShoppingCartItemDto(
                dto.getProductId(), dto.getQuantity()
        );

        final ShoppingCartItemDto existingItem = shoppingCartFromSession.getItems().stream()
                .filter(it -> it.getProductId().equals(item.getProductId()))
                .findFirst().orElse(null);

        if (existingItem != null) {
            if (!dto.getReplace()) {
                final int sum = item.getQuantity() + existingItem.getQuantity();
                if (sum <= General.MAX_PROD_QUANTITY) {
                    item.setQuantity(sum);
                }
            }
            shoppingCartFromSession.getItems().remove(existingItem);
        }

        shoppingCartFromSession.getItems().add(item);

        if (this.principal.isUserPresent()) {
            final ShoppingCart shoppingCart = this.getOrCreateShoppingCart();
            this.mergeIntoDb(shoppingCart, shoppingCartFromSession);
        }

        return this.fetchItems(shoppingCartFromSession.getItems());
    }

    @Override
    @Transactional
    public List<ShoppingCartItemDetailedDto> removeItem(String sessionId, Long productId) {
        if (this.principal.isUserPresent()) {
            this.mergeIntoSession(sessionId);
        }

        final ShoppingCartDto shoppingCartFromSession = this.getShoppingCartFromSession(sessionId);
        shoppingCartFromSession.getItems().removeIf(dto -> dto.getProductId().equals(productId));

        if (this.principal.isUserPresent()) {
            final ShoppingCart shoppingCart = this.getOrCreateShoppingCart();
            this.mergeIntoDb(shoppingCart, shoppingCartFromSession);
        }

        return this.fetchItems(shoppingCartFromSession.getItems());
    }

    private List<ShoppingCartItemDto> mergeIntoSession(String sessionId) {
        final User user = this.getUser();

        final ShoppingCartDto shoppingCartFromSession = this.getShoppingCartFromSession(sessionId);
        final ShoppingCart shoppingCart = this.shoppingCartRepository.findByUserId(user.getId());

        if (shoppingCart == null) {
            return shoppingCartFromSession.getItems();
        }

        this.mergeIntoSession(shoppingCart, shoppingCartFromSession);

        return shoppingCartFromSession.getItems();
    }

    private void mergeIntoSession(ShoppingCart db, ShoppingCartDto session) {
        final Set<Long> sessionProductIds = session.getItems().stream()
                .map(ShoppingCartItemDto::getProductId)
                .collect(Collectors.toSet());

        for (ShoppingCartItem item : db.getItems()) {
            if (!sessionProductIds.contains(item.getProductId())) {
                session.getItems().add(new ShoppingCartItemDto(
                        item.getProductId(), item.getQuantity()
                ));
            }
        }
    }

    private void mergeIntoDb(ShoppingCart db, ShoppingCartDto session) {
        final Set<Long> sessionProductIds = session.getItems().stream()
                .map(ShoppingCartItemDto::getProductId)
                .collect(Collectors.toSet());

        final Set<Long> dbProductIds = db.getItems().stream()
                .map(ShoppingCartItem::getProductId)
                .collect(Collectors.toSet());

        final Map<Long, Integer> sessionProds = session.getItems().stream()
                .collect(Collectors.toMap(
                        ShoppingCartItemDto::getProductId,
                        ShoppingCartItemDto::getQuantity)
                );

        final List<ShoppingCartItem> itemsToRemove = new ArrayList<>();
        db.getItems().removeIf(item -> {
            if (!sessionProductIds.contains(item.getProductId())) {
                itemsToRemove.add(item);
                return true;
            }

            return false;
        });

        for (ShoppingCartItem item : db.getItems()) {
            item.setQuantity(sessionProds.get(item.getProductId()));
        }

        final List<ShoppingCartItem> itemsToPersist = new ArrayList<>();
        for (ShoppingCartItemDto item : session.getItems()) {
            if (!dbProductIds.contains(item.getProductId())) {
                itemsToPersist.add(new ShoppingCartItem(
                        db.getId(), item.getProductId(), item.getQuantity()
                ));
            }
        }

        db.getItems().addAll(itemsToPersist);
        db.setLastModified(LocalDateTime.now());
        this.shoppingCartItemRepository.persist(itemsToPersist);
        this.shoppingCartItemRepository.remove(itemsToRemove);
        this.shoppingCartRepository.merge(db);
    }

    private ShoppingCartDto getShoppingCartFromSession(String sessionId) {
        final ShoppingCartDto shoppingCartDto = this.sessionItems.get(sessionId);
        if (shoppingCartDto == null) {
            throw new ShoppingCartSessionException(INVALID_SHOPPING_CART_SESSION);
        }

        shoppingCartDto.setLastModified(LocalDateTime.now());
        return shoppingCartDto;
    }

    private List<ShoppingCartItemDetailedDto> fetchItems(List<ShoppingCartItemDto> items) {
        final Map<Long, Integer> prodQuantity = items.stream().collect(Collectors.toMap(
                ShoppingCartItemDto::getProductId,
                ShoppingCartItemDto::getQuantity
        ));

        if (prodQuantity.isEmpty()) {
            return new ArrayList<>();
        }

        return this.productService.findAllByIdIn(prodQuantity.keySet()).stream()
                .map(prod -> new ShoppingCartItemDetailedDto(
                        this.modelMapper.map(prod, ProductDto.class),
                        prodQuantity.get(prod.getId())
                ))
                .collect(Collectors.toList());
    }

    private ShoppingCart getOrCreateShoppingCart() {
        final User user = this.getUser();
        ShoppingCart shoppingCart = this.shoppingCartRepository.findByUserId(user.getId());

        if (shoppingCart != null) {
            return shoppingCart;
        }

        shoppingCart = new ShoppingCart();
        shoppingCart.setLastModified(LocalDateTime.now());
        shoppingCart.setUserId(user.getId());
        shoppingCart.setItems(new ArrayList<>());
        this.shoppingCartRepository.persist(shoppingCart);

        return shoppingCart;
    }

    private User getUser() {
        final User user = (User) this.principal.getUser();

        if (user == null) {
            throw new ApiException("Trying to merge shopping cart without any user.");
        }

        return user;
    }

    @Data
    private static class ShoppingCartDto {

        private List<ShoppingCartItemDto> items;

        private LocalDateTime lastModified;
    }
}
