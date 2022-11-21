package com.cyecize.app.api.user.address;

import com.cyecize.app.api.user.User;
import com.cyecize.summer.common.annotations.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserAddressServiceImpl implements UserAddressService {

    private final UserAddressRepository userAddressRepository;

    @Override
    public UserAddress findByUserAndId(User user, Long addressId) {
        return this.userAddressRepository.findByUserIdAndAddressId(user.getId(), addressId);
    }
}
