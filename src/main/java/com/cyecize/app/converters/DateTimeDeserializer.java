package com.cyecize.app.converters;

import com.cyecize.summer.common.annotations.Component;
import com.fasterxml.jackson.databind.util.StdConverter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeDeserializer extends StdConverter<String, LocalDateTime> {

    @Override
    public LocalDateTime convert(String source) {
        try {
            return LocalDateTime.parse(source, DateTimeFormatter.ISO_DATE_TIME);
        } catch (Exception ignored) {
        }

        return null;
    }
}
