package com.cyecize.app.web;

import com.cyecize.app.api.product.Product;
import com.cyecize.app.api.product.ProductDto;
import com.cyecize.app.api.product.ProductDtoDetailed;
import com.cyecize.app.api.product.ProductIdDataAdapter;
import com.cyecize.app.api.product.ProductQuery;
import com.cyecize.app.api.product.ProductService;
import com.cyecize.app.api.product.productspec.SpecificationTypeDto;
import com.cyecize.app.api.product.productspec.SpecificationTypeQuery;
import com.cyecize.app.api.product.productspec.SpecificationTypeService;
import com.cyecize.app.constants.Endpoints;
import com.cyecize.app.constants.General;
import com.cyecize.app.util.Page;
import com.cyecize.summer.areas.validation.annotations.ConvertedBy;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.GetMapping;
import com.cyecize.summer.common.annotations.routing.PathVariable;
import com.cyecize.summer.common.annotations.routing.PostMapping;
import com.cyecize.summer.common.annotations.routing.RequestMapping;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@Controller
@RequestMapping(value = "", produces = General.APPLICATION_JSON)
@RequiredArgsConstructor
public class ProductController {

    private final ModelMapper modelMapper;

    private final ProductService productService;

    private final SpecificationTypeService specificationTypeService;

    @PostMapping(Endpoints.PRODUCTS)
    public Page<ProductDto> searchProducts(@Valid ProductQuery productQuery) {
        return this.productService.searchProducts(productQuery)
                .map(product -> this.modelMapper.map(product, ProductDto.class));
    }

    @GetMapping(Endpoints.PRODUCT)
    public ProductDtoDetailed getProduct(@PathVariable(value = "id")
                                         @ConvertedBy(ProductIdDataAdapter.class) Product product) {
        return this.modelMapper.map(product, ProductDtoDetailed.class);
    }

    @PostMapping(Endpoints.SPECIFICATION_TYPES)
    public Page<SpecificationTypeDto> searchSpecificationTypes(@Valid SpecificationTypeQuery query) {
        return this.specificationTypeService.findAll(query)
                .map(st -> this.modelMapper.map(st, SpecificationTypeDto.class));
    }
}
