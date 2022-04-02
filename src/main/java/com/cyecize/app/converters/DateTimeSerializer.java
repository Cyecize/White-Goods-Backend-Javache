package com.cyecize.app.converters;

import com.cyecize.summer.common.annotations.Component;
import com.fasterxml.jackson.databind.util.StdConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeSerializer extends StdConverter<LocalDateTime, String> {
    @Override
    public String convert(LocalDateTime value) {
        return value.format(DateTimeFormatter.ISO_DATE_TIME);
    }
}
