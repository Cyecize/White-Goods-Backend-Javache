package com.cyecize.app.api.mail;

import com.cyecize.app.api.question.QuestionDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class EmailTemplate<T> {
    public static final EmailTemplate<QuestionDto> QUESTION = new EmailTemplate<>(
            "mailing/new-question.html.twig",
            "New question from White Goods Store",
            QuestionDto.class
    );

    private final String templateName;

    private final String subject;

    private final Class<T> viewModelType;
}
