package com.cyecize.app.api.contacts;

import lombok.Data;

@Data
public class ContactsDto {
    private final String email;
    private final String phoneNumber;
    private final String whatsapp;
    private final String facebook;
    private final String youtube;
}
