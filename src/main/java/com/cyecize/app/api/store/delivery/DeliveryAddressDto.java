package com.cyecize.app.api.store.delivery;

import lombok.Data;

@Data
public class DeliveryAddressDto {
    private String fullName;

    private String email;

    private String phoneNumber;

    private String country;

    private String city;

    private String addressLine;

    private String notes;
}
