package com.cyecize.app.api.user.address;

import com.cyecize.app.api.store.delivery.CreateAddressDto;
import com.cyecize.app.api.user.User;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Service;
import java.util.List;
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

    @Override
    public List<UserAddress> findByUserId(Long userId) {
        return this.userAddressRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public UserAddress update(UserAddress address, CreateAddressDto dto) {
        //TODO: model merger

        address.setFullName(dto.getFullName());
        address.setEmail(dto.getEmail());
        address.setPhoneNumber(dto.getPhoneNumber());
        address.setCountry(dto.getCountry());
        address.setCity(dto.getCity());
        address.setAddressLine(dto.getAddressLine());
        address.setNotes(dto.getNotes());

        if (BooleanUtils.isTrue(dto.getMakePreferred()) && !address.isPreferredAddress()) {
            this.userAddressRepository.setPreferredFalseWhereUserIdEquals(address.getUserId());
            address.setPreferredAddress(true);
        }

        return this.userAddressRepository.merge(address);
    }
}
