package com.cyecize.app.api.user.address;

import com.cyecize.app.api.store.delivery.CreateAddressDto;
import com.cyecize.app.api.user.User;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Service;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.modelmapper.ModelMapper;

@Service
@RequiredArgsConstructor
public class UserAddressServiceImpl implements UserAddressService {

    private final UserAddressRepository userAddressRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public UserAddress createAddress(User user, CreateAddressDto dto) {
        final UserAddress userAddress = this.modelMapper.map(dto, UserAddress.class);
        userAddress.setUserId(user.getId());
        if (BooleanUtils.isTrue(dto.getMakePreferred())) {
            this.userAddressRepository.setPreferredFalseWhereUserIdEquals(user.getId());
            userAddress.setPreferredAddress(true);
        }

        return this.userAddressRepository.persist(userAddress);
    }

    @Override
    public UserAddress findByUserAndId(User user, Long addressId) {
        return this.userAddressRepository.findByUserIdAndAddressId(user.getId(), addressId);
    }
}
