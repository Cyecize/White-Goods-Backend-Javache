package com.cyecize.app.api.user.address.converter;

import com.cyecize.app.api.user.User;
import com.cyecize.app.api.user.address.UserAddress;
import com.cyecize.app.api.user.address.UserAddressService;
import com.cyecize.summer.areas.security.models.Principal;
import com.cyecize.summer.common.annotations.Component;
import com.fasterxml.jackson.databind.util.StdConverter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;

@Component
@RequiredArgsConstructor
public class UserAddressIdDeserializer extends StdConverter<String, UserAddress> {

    private final UserAddressService userAddressService;

    private final Principal principal;

    @Override
    public UserAddress convert(String value) {
        final long id = NumberUtils.toLong(value, -1L);
        if (id == -1L) {
            return null;
        }

        return this.userAddressService.findByUserAndId((User) this.principal.getUser(), id);
    }
}
