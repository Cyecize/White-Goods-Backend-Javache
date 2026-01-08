package com.cyecize.app.api.question;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class QuestionDto {

    private Long id;

    private String fullName;

    private String email;

    private String phoneNumber;

    private String message;

    private LocalDateTime date;
}
