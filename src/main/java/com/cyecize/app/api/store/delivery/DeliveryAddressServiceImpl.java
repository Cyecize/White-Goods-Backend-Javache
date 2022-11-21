package com.cyecize.app.api.store.delivery;

import com.cyecize.app.api.user.address.UserAddress;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Service;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@Service
@RequiredArgsConstructor
public class DeliveryAddressServiceImpl implements DeliveryAddressService {

    private final ModelMapper modelMapper;

    private final DeliveryAddressRepository deliveryAddressRepository;

    @Override
    @Transactional
    public DeliveryAddress createAddress(CreateAddressDto dto) {
        final DeliveryAddress address = this.modelMapper.map(dto, DeliveryAddress.class);

        return this.deliveryAddressRepository.persist(address);
    }

    @Override
    @Transactional
    public DeliveryAddress createAddress(UserAddress address) {
        final DeliveryAddress addr = this.modelMapper.map(address, DeliveryAddress.class);
        addr.setId(null);
        return this.deliveryAddressRepository.persist(addr);
    }
}
