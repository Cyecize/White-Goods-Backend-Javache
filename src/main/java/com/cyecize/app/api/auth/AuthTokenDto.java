package com.cyecize.app.api.auth;

import com.cyecize.app.converters.DateTimeConverter;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class AuthTokenDto {

    private String id;

    @DateTimeConverter
    private LocalDateTime lastAccessDate;
}
