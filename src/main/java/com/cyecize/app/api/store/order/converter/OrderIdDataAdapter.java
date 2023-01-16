package com.cyecize.app.api.store.order.converter;

import com.cyecize.app.api.store.order.Order;
import com.cyecize.app.api.store.order.OrderService;
import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.summer.areas.validation.interfaces.DataAdapter;
import com.cyecize.summer.common.annotations.Component;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Component
@RequiredArgsConstructor
public class OrderIdDataAdapter implements DataAdapter<Order> {

    private final OrderService orderService;

    @Override
    public Order resolve(String idStr, HttpSoletRequest httpSoletRequest) {
        if (!StringUtils.isNumeric(idStr)) {
            return null;
        }

        return this.orderService.findById(Long.parseLong(idStr.trim()));
    }
}
