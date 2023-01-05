package com.cyecize.app.web;

import com.cyecize.app.api.store.delivery.CreateAddressDto;
import com.cyecize.app.api.user.User;
import com.cyecize.app.api.user.address.UserAddress;
import com.cyecize.app.api.user.address.UserAddressDto;
import com.cyecize.app.api.user.address.UserAddressService;
import com.cyecize.app.constants.Endpoints;
import com.cyecize.app.constants.General;
import com.cyecize.app.error.NotFoundApiException;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.http.HttpStatus;
import com.cyecize.summer.areas.security.annotations.PreAuthorize;
import com.cyecize.summer.areas.security.enums.AuthorizationType;
import com.cyecize.summer.areas.security.models.Principal;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.DeleteMapping;
import com.cyecize.summer.common.annotations.routing.GetMapping;
import com.cyecize.summer.common.annotations.routing.PathVariable;
import com.cyecize.summer.common.annotations.routing.PostMapping;
import com.cyecize.summer.common.annotations.routing.PutMapping;
import com.cyecize.summer.common.annotations.routing.RequestMapping;
import com.cyecize.summer.common.models.JsonResponse;
import java.util.List;
import java.util.stream.Collectors;
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

    @GetMapping(Endpoints.USER_ADDRESSES)
    public List<UserAddressDto> getMyAddresses(Principal principal) {
        return this.userAddressService.findByUserId(((User) principal.getUser()).getId())
                .stream()
                .map(addr -> this.modelMapper.map(addr, UserAddressDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping(Endpoints.USER_ADDRESS)
    public UserAddressDto getAddress(@PathVariable("id") Long addressId, Principal principal) {
        return this.modelMapper.map(this.fetchAddress(addressId, principal), UserAddressDto.class);
    }

    @Transactional
    @PutMapping(Endpoints.USER_ADDRESS)
    public UserAddressDto editAddress(
            @PathVariable("id") Long addressId,
            Principal principal,
            @Valid CreateAddressDto dto) {
        final UserAddress oldAddress = this.fetchAddress(addressId, principal);
        final UserAddress address = this.userAddressService.update(oldAddress, dto);

        return this.modelMapper.map(address, UserAddressDto.class);
    }

    @Transactional
    @DeleteMapping(Endpoints.USER_ADDRESS)
    public JsonResponse deleteAddress(@PathVariable("id") Long addressId, Principal principal) {
        final UserAddress address = this.fetchAddress(addressId, principal);
        this.userAddressService.delete(address);
        return new JsonResponse(HttpStatus.OK)
                .addAttribute("message", "Address Removed!");
    }

    private UserAddress fetchAddress(Long addressId, Principal principal) {
        final UserAddress address = this.userAddressService.findByUserAndId(
                (User) principal.getUser(),
                addressId
        );

        if (address == null) {
            throw new NotFoundApiException("Invalid address id!");
        }

        return address;
    }
}
