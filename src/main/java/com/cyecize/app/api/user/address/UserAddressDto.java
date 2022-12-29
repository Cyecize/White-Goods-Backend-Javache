package com.cyecize.app.api.user.address;

import lombok.Data;

@Data
public class UserAddressDto {

    private Long id;

    private Long userId;

    private String fullName;

    private String email;

    private String phoneNumber;

    private String country;

    private String city;

    private String addressLine;

    private String notes;

    private Boolean preferredAddress;
}
