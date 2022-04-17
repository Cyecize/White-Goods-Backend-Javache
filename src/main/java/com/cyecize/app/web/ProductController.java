package com.cyecize.app.web;

import com.cyecize.app.api.product.Product;
import com.cyecize.app.api.product.ProductQuery;
import com.cyecize.app.api.product.ProductService;
import com.cyecize.app.api.product.converter.ProductIdDataAdapter;
import com.cyecize.app.api.product.dto.CreateProductDto;
import com.cyecize.app.api.product.dto.EditProductDto;
import com.cyecize.app.api.product.dto.ProductDto;
import com.cyecize.app.api.product.dto.ProductDtoDetailed;
import com.cyecize.app.api.user.User;
import com.cyecize.app.constants.Endpoints;
import com.cyecize.app.constants.General;
import com.cyecize.app.util.Page;
import com.cyecize.summer.areas.security.annotations.PreAuthorize;
import com.cyecize.summer.areas.security.enums.AuthorizationType;
import com.cyecize.summer.areas.security.models.Principal;
import com.cyecize.summer.areas.validation.annotations.ConvertedBy;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.GetMapping;
import com.cyecize.summer.common.annotations.routing.PathVariable;
import com.cyecize.summer.common.annotations.routing.PostMapping;
import com.cyecize.summer.common.annotations.routing.PutMapping;
import com.cyecize.summer.common.annotations.routing.RequestMapping;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@Controller
@PreAuthorize(role = General.ROLE_ADMIN)
@RequestMapping(value = "", produces = General.APPLICATION_JSON)
@RequiredArgsConstructor
public class ProductController {

    private final ModelMapper modelMapper;

    private final ProductService productService;

    @PostMapping(Endpoints.PRODUCTS)
    @PreAuthorize(AuthorizationType.ANY)
    public Page<ProductDto> searchProducts(@Valid ProductQuery productQuery, Principal principal) {
        return this.productService.searchProducts(productQuery, (User) principal.getUser())
                .map(product -> this.modelMapper.map(product, ProductDto.class));
    }

    @PostMapping(Endpoints.PRODUCT_CREATE)
    public ProductDto createProduct(@Valid CreateProductDto dto) {
        return this.modelMapper.map(this.productService.createProduct(dto), ProductDto.class);
    }

    @GetMapping(Endpoints.PRODUCT)
    @PreAuthorize(AuthorizationType.ANY)
    public ProductDtoDetailed getProduct(@PathVariable(value = "id")
                                         @ConvertedBy(ProductIdDataAdapter.class) Product product) {
        return this.modelMapper.map(product, ProductDtoDetailed.class);
    }

    @PutMapping(Endpoints.PRODUCT)
    public ProductDtoDetailed editProduct(@PathVariable(value = "id")
                                          @ConvertedBy(ProductIdDataAdapter.class) Product product,
                                          @Valid EditProductDto dto) {
        return this.modelMapper.map(this.productService.editProduct(product.getId(), dto), ProductDtoDetailed.class);
    }
}
