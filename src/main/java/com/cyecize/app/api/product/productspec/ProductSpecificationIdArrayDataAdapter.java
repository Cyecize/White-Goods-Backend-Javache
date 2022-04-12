package com.cyecize.app.api.product.productspec;

import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.summer.areas.validation.interfaces.DataAdapter;
import com.cyecize.summer.common.annotations.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductSpecificationIdArrayDataAdapter implements DataAdapter<List<ProductSpecification>> {
    private final ObjectMapper objectMapper;
    private final ProductSpecificationService productSpecificationService;

    @Override
    public List<ProductSpecification> resolve(String param, HttpSoletRequest request) {
        if (param == null || param.trim().equals("[]")) {
            return new ArrayList<>();
        }

        try {
           final List<Long> ids = List.of(objectMapper.readValue(param, Long[].class));
           return this.productSpecificationService.findAllSpecificationsById(ids);
        } catch (JsonProcessingException ignored) {
            return new ArrayList<>();
        }
    }
}
