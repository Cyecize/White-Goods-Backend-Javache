package com.cyecize.app.web;

import com.cyecize.app.api.store.delivery.CreateAddressDto;
import com.cyecize.app.api.user.User;
import com.cyecize.app.api.user.address.UserAddress;
import com.cyecize.app.api.user.address.UserAddressDto;
import com.cyecize.app.api.user.address.UserAddressService;
import com.cyecize.app.constants.Endpoints;
import com.cyecize.app.constants.General;
import com.cyecize.summer.areas.security.annotations.PreAuthorize;
import com.cyecize.summer.areas.security.enums.AuthorizationType;
import com.cyecize.summer.areas.security.models.Principal;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.PostMapping;
import com.cyecize.summer.common.annotations.routing.RequestMapping;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@Controller
@RequiredArgsConstructor
@PreAuthorize(AuthorizationType.LOGGED_IN)
@RequestMapping(value = "", produces = General.APPLICATION_JSON)
public class UserAddressController {

    private final UserAddressService userAddressService;

    private final ModelMapper modelMapper;

    @PostMapping(Endpoints.USER_ADDRESSES)
    public UserAddressDto addAddress(@Valid CreateAddressDto dto, Principal principal) {
        final UserAddress address = this.userAddressService.createAddress(
                (User) principal.getUser(),
                dto
        );
        return this.modelMapper.map(address, UserAddressDto.class);
    }
}
