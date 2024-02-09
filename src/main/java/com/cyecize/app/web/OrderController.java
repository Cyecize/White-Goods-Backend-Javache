package com.cyecize.app.web;

import com.cyecize.app.api.store.order.CreateOrderAnonDto;
import com.cyecize.app.api.store.order.CreateOrderLoggedInDto;
import com.cyecize.app.api.store.order.OrderQuery;
import com.cyecize.app.api.store.order.OrderService;
import com.cyecize.app.api.store.order.dto.OrderDto;
import com.cyecize.app.api.store.order.dto.OrderDtoSimple;
import com.cyecize.app.api.store.order.dto.UpdateOrderStatusDto;
import com.cyecize.app.api.user.RoleType;
import com.cyecize.app.api.user.User;
import com.cyecize.app.api.user.address.UserAddress;
import com.cyecize.app.constants.Endpoints;
import com.cyecize.app.constants.General;
import com.cyecize.app.error.NotFoundApiException;
import com.cyecize.app.util.Page;
import com.cyecize.http.HttpStatus;
import com.cyecize.summer.areas.security.annotations.PreAuthorize;
import com.cyecize.summer.areas.security.enums.AuthorizationType;
import com.cyecize.summer.areas.security.models.Principal;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.GetMapping;
import com.cyecize.summer.common.annotations.routing.PathVariable;
import com.cyecize.summer.common.annotations.routing.PostMapping;
import com.cyecize.summer.common.annotations.routing.PutMapping;
import com.cyecize.summer.common.annotations.routing.RequestMapping;
import com.cyecize.summer.common.models.JsonResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "", produces = General.APPLICATION_JSON)
public class OrderController {

    private final OrderService orderService;

    private final ModelMapper modelMapper;

    @PostMapping(Endpoints.ORDERS_ANON)
    @PreAuthorize(AuthorizationType.ANONYMOUS)
    public JsonResponse checkOutAnon(@Valid CreateOrderAnonDto dto) {
        final CreateOrderLoggedInDto loggedInDto = new CreateOrderLoggedInDto(
                dto.getSessionId(),
                this.modelMapper.map(dto.getAddress(), UserAddress.class),
                dto.getUserAgreedPrice()
        );

        this.orderService.createOrder(loggedInDto, null);

        return this.successfulOrderResponse();
    }

    @PostMapping(Endpoints.ORDERS)
    @PreAuthorize(AuthorizationType.LOGGED_IN)
    public JsonResponse checkOutLogged(@Valid CreateOrderLoggedInDto dto, Principal principal) {
        this.orderService.createOrder(dto, (User) principal.getUser());
        return this.successfulOrderResponse();
    }

    @GetMapping(Endpoints.ORDER)
    @PreAuthorize(AuthorizationType.LOGGED_IN)
    public OrderDto getOrder(@PathVariable("id") Long orderId, Principal principal) {
        final OrderDto orderDto;
        if (principal.hasAuthority(RoleType.ROLE_ADMIN.name())) {
            orderDto = this.orderService.getOrder(orderId);
        } else {
            orderDto = this.orderService.getOrder(orderId, ((User) principal.getUser()).getId());
        }

        if (orderDto == null) {
            throw new NotFoundApiException("Order not found!");
        }

        return orderDto;
    }

    @PutMapping(Endpoints.ORDERS_STATUS)
    @PreAuthorize(role = General.ROLE_ADMIN)
    public OrderDto updateStatus(@Valid UpdateOrderStatusDto dto) {
        this.orderService.changeOrderStatus(dto);
        return this.orderService.getOrder(dto.getOrder().getId());
    }

    @PostMapping(Endpoints.ORDERS_SEARCH)
    @PreAuthorize(AuthorizationType.LOGGED_IN)
    public Page<OrderDtoSimple> searchOrders(@Valid OrderQuery query, Principal principal) {
        return this.orderService.searchOrders(query, ((User) principal.getUser()).getId())
                .map(order -> this.modelMapper.map(order, OrderDtoSimple.class));
    }

    private JsonResponse successfulOrderResponse() {
        return new JsonResponse()
                .setStatusCode(HttpStatus.CREATED)
                .addAttribute("message", "Order was created!");
    }
}
