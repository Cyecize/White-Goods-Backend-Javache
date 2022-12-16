package com.cyecize.app.web;

import com.cyecize.app.api.store.order.CreateOrderAnonDto;
import com.cyecize.app.api.store.order.CreateOrderLoggedInDto;
import com.cyecize.app.api.store.order.OrderQuery;
import com.cyecize.app.api.store.order.OrderService;
import com.cyecize.app.api.store.order.dto.OrderDtoSimple;
import com.cyecize.app.api.user.User;
import com.cyecize.app.constants.Endpoints;
import com.cyecize.app.constants.General;
import com.cyecize.app.util.Page;
import com.cyecize.http.HttpStatus;
import com.cyecize.summer.areas.security.annotations.PreAuthorize;
import com.cyecize.summer.areas.security.enums.AuthorizationType;
import com.cyecize.summer.areas.security.models.Principal;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.PostMapping;
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
        this.orderService.createOrder(dto);
        return this.successfulOrderResponse();
    }

    @PostMapping(Endpoints.ORDERS)
    @PreAuthorize(AuthorizationType.LOGGED_IN)
    public JsonResponse checkOutLogged(@Valid CreateOrderLoggedInDto dto, Principal principal) {
        this.orderService.createOrder(dto, (User) principal.getUser());
        return this.successfulOrderResponse();
    }

    @PostMapping(Endpoints.ORDERS_SEARCH)
    @PreAuthorize(role = General.ROLE_ADMIN)
    public Page<OrderDtoSimple> searchOrders(@Valid OrderQuery query) {
        return this.orderService.searchOrders(query, null)
                .map(order -> this.modelMapper.map(order, OrderDtoSimple.class));
    }

    private JsonResponse successfulOrderResponse() {
        return new JsonResponse()
                .setStatusCode(HttpStatus.CREATED)
                .addAttribute("message", "Order was created!");
    }
}
