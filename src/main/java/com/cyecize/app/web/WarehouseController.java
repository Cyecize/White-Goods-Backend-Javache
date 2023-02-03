package com.cyecize.app.web;

import com.cyecize.app.api.warehouse.QuantityUpdateQuery;
import com.cyecize.app.api.warehouse.WarehouseService;
import com.cyecize.app.api.warehouse.dto.CreateQuantityUpdateDto;
import com.cyecize.app.api.warehouse.dto.CreateWarehouseDeliveryDto;
import com.cyecize.app.api.warehouse.dto.QuantityUpdateDto;
import com.cyecize.app.constants.Endpoints;
import com.cyecize.app.constants.General;
import com.cyecize.app.error.ApiException;
import com.cyecize.app.util.Page;
import com.cyecize.http.HttpStatus;
import com.cyecize.summer.areas.security.annotations.PreAuthorize;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.PathVariable;
import com.cyecize.summer.common.annotations.routing.PostMapping;
import com.cyecize.summer.common.annotations.routing.PutMapping;
import com.cyecize.summer.common.annotations.routing.RequestMapping;
import com.cyecize.summer.common.models.JsonResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@Controller
@RequestMapping(value = "", produces = General.APPLICATION_JSON)
@PreAuthorize(role = General.ROLE_ADMIN)
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    private final ModelMapper modelMapper;

    @PutMapping(Endpoints.WAREHOUSE_ITEM)
    public JsonResponse updateItemQty(@PathVariable("id") Long productId,
            @Valid CreateQuantityUpdateDto dto) {
        if (!productId.equals(dto.getProductId())) {
            throw new ApiException("Discrepancy between path variable prod id and prod id in DTO!");
        }

        this.warehouseService.updateQuantity(dto);
        return new JsonResponse(HttpStatus.OK).addAttribute("message", "Item Updated!");
    }

    @PostMapping(Endpoints.WAREHOUSE_ITEM)
    public Page<QuantityUpdateDto> getItemHistory(@PathVariable("id") Long productId,
            @Valid QuantityUpdateQuery query) {
        return this.warehouseService.getQuantityUpdateHistory(productId, query)
                .map(qu -> this.modelMapper.map(qu, QuantityUpdateDto.class));
    }

    @PostMapping(Endpoints.WAREHOUSE_DELIVERY)
    public JsonResponse performDelivery(@Valid CreateWarehouseDeliveryDto dto) {
        this.warehouseService.performDelivery(dto);
        return new JsonResponse(HttpStatus.CREATED)
                .addAttribute("message", "Delivery completed!");
    }
}
