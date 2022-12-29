package com.cyecize.app.api.user.address;

import com.cyecize.app.api.store.delivery.CreateAddressDto;
import com.cyecize.app.api.user.User;

public interface UserAddressService {

    UserAddress createAddress(User user, CreateAddressDto dto);

    UserAddress findByUserAndId(User user, Long id);
}
