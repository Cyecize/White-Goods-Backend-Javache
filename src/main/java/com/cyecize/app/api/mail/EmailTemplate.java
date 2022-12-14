package com.cyecize.app.api.mail;

import com.cyecize.app.api.question.QuestionDto;
import com.cyecize.app.api.store.order.dto.OrderDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class EmailTemplate<T> {

    public static final EmailTemplate<QuestionDto> QUESTION = new EmailTemplate<>(
            "mailing/new-question.html.twig",
            "new.question",
            QuestionDto.class
    );

    public static final EmailTemplate<OrderDto> NEW_ORDER_ADMINS = new EmailTemplate<>(
            "mailing/new-order.html.twig",
            "new.order",
            OrderDto.class
    );

    private final String templateName;

    private final String subject;

    private final Class<T> viewModelType;
}
