package com.cyecize.app.api.user.address;

import com.cyecize.app.api.user.User;

public interface UserAddressService {

    UserAddress findByUserAndId(User user, Long id);
}
