package com.cyecize.app.api.contacts;

import lombok.Data;

@Data
public class ContactsDto {

    private final String email;
    private final String phoneNumber;
    private final String whatsapp;
    private final String facebook;
    private final String youtube;
    private final String instagram;
    private final String deliveryCompanies;
    private final String googleMapsSrc;
    private final String addressBg;
    private final String addressEn;
}
