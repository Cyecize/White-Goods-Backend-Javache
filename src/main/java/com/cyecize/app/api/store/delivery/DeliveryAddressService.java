package com.cyecize.app.api.store.delivery;

import com.cyecize.app.api.user.address.UserAddress;

public interface DeliveryAddressService {

    DeliveryAddress createAddress(CreateAddressDto dto);

    DeliveryAddress createAddress(UserAddress address);
}
