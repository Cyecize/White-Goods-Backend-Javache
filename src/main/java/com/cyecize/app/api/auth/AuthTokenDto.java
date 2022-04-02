package com.cyecize.app.api.auth;

import com.cyecize.app.converters.DateTimeConverter;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuthTokenDto {
    private String id;

    @DateTimeConverter
    private LocalDateTime lastAccessDate;
}
