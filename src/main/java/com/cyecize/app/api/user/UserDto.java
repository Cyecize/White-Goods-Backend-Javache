package com.cyecize.app.api.user;

import com.cyecize.app.converters.DateTimeConverter;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserDto {
    private Long id;

    private String username;

    private String email;

    @DateTimeConverter
    private LocalDateTime dateRegistered;

    private List<RoleDto> roles;

    @Data
    public static class RoleDto {
        @JsonValue
        private String role;
    }
}
