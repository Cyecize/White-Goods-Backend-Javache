package com.cyecize.app.api.mail;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class EmailTemplate<T> {

    private final String templateName;

    private final String subject;

    private final Class<T> viewModelType;
}
