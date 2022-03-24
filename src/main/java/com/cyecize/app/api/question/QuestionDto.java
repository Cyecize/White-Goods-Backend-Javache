package com.cyecize.app.api.question;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuestionDto {
    private Long id;

    private String fullName;

    private String email;

    private String phoneNumber;

    private String message;

    private LocalDateTime date;
}
