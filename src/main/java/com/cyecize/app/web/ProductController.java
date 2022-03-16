package com.cyecize.app.web;

import com.cyecize.app.api.product.Product;
import com.cyecize.app.api.product.ProductDto;
import com.cyecize.app.api.product.ProductIdDataAdapter;
import com.cyecize.app.constants.Endpoints;
import com.cyecize.app.constants.General;
import com.cyecize.summer.areas.validation.annotations.ConvertedBy;
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.GetMapping;
import com.cyecize.summer.common.annotations.routing.PathVariable;
import com.cyecize.summer.common.annotations.routing.RequestMapping;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@Controller
@RequestMapping(value = "", produces = General.APPLICATION_JSON)
@RequiredArgsConstructor
public class ProductController {

    private final ModelMapper modelMapper;

    @GetMapping(Endpoints.PRODUCT)
    public ProductDto getProduct(@PathVariable(value = "id")
                                 @ConvertedBy(ProductIdDataAdapter.class) Product product) {
        return this.modelMapper.map(product, ProductDto.class);
    }
}
