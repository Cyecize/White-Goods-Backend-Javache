package com.cyecize.app.api.contacts;

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

    @Configuration("contacts.whatsapp")
    private final String whatsapp;

    @Configuration("contacts.facebook")
    private final String facebook;

    @Configuration("contacts.youtube")
    private final String youtube;

    @Override
    public ContactsDto getContacts() {
        return new ContactsDto(
                this.email,
                this.phoneNumber,
                this.whatsapp,
                this.facebook,
                this.youtube
        );
    }
}