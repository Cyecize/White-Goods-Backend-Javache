package com.cyecize.app.converters;

import com.cyecize.summer.common.annotations.Component;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.SneakyThrows;

import java.io.IOException;

@Component
public class GenericEnumConverterImpl extends JsonDeserializer<Object> {

    @Override
    public Object deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        final String currentName = parser.getCurrentName();
        final Class<?> modelType = parser.getCurrentValue().getClass();

        final Class<?> fieldType = this.findFieldType(currentName, modelType);

        if (!fieldType.isEnum()) {
            return null;
        }

        final String providedValue = parser.getValueAsString();

        final Enum<?>[] constants = (Enum<?>[]) fieldType.getEnumConstants();
        for (Enum<?> constant : constants) {
            if (constant.name().equalsIgnoreCase(providedValue)) {
                return constant;
            }
        }

        return null;
    }

    @SneakyThrows
    private Class<?> findFieldType(String fieldName, Class<?> model) {
        return model.getDeclaredField(fieldName).getType();
    }
}
