package com.cyecize.app.web;

import com.cyecize.app.api.product.ProductCategoryDto;
import com.cyecize.app.api.product.ProductCategoryService;
import com.cyecize.app.constants.Endpoints;
import com.cyecize.app.constants.General;
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.GetMapping;
import com.cyecize.summer.common.annotations.routing.RequestMapping;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "", produces = General.APPLICATION_JSON)
@RequiredArgsConstructor
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;
    private final ModelMapper modelMapper;

    @GetMapping(Endpoints.CATEGORIES)
    public List<ProductCategoryDto> getCategories() {
        return this.productCategoryService.findAllCategories().stream()
                .map(productCategory -> this.modelMapper.map(productCategory, ProductCategoryDto.class))
                .collect(Collectors.toList());
    }
}
