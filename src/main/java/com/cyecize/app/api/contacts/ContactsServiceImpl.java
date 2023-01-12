package com.cyecize.app.api.contacts;

import com.cyecize.ioc.annotations.Nullable;
import com.cyecize.summer.common.annotations.Configuration;
import com.cyecize.summer.common.annotations.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContactsServiceImpl implements ContactsService {

    @Configuration("contacts.email")
    private final String email;

    @Configuration("contacts.phoneNumber")
    private final String phoneNumber;

    @Nullable
    @Configuration("contacts.whatsapp")
    private final String whatsapp;

    @Nullable
    @Configuration("contacts.facebook")
    private final String facebook;

    @Nullable
    @Configuration("contacts.youtube")
    private final String youtube;

    @Nullable
    @Configuration("contacts.delivery.companies")
    private final String deliveryCompanies;

    @Nullable
    @Configuration("contacts.instagram")
    private final String instagram;

    @Configuration("contacts.map.src")
    private final String googleMapsSrc;

    @Configuration("contacts.address.bg")
    private final String addressBg;

    @Configuration("contacts.address.en")
    private final String addressEn;

    @Override
    public ContactsDto getContacts() {
        return new ContactsDto(
                this.email,
                this.phoneNumber,
                this.whatsapp,
                this.facebook,
                this.youtube,
                this.instagram,
                this.deliveryCompanies,
                this.googleMapsSrc,
                this.addressBg,
                this.addressEn
        );
    }
}
