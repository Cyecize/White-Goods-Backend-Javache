package com.cyecize.app.api.user.address;

import com.cyecize.app.api.store.delivery.CreateAddressDto;
import com.cyecize.app.api.user.User;
import java.util.List;

public interface UserAddressService {

    UserAddress createAddress(User user, CreateAddressDto dto);

    UserAddress findByUserAndId(User user, Long id);

    List<UserAddress> findByUserId(Long userId);

    UserAddress update(UserAddress address, CreateAddressDto dto);
}
