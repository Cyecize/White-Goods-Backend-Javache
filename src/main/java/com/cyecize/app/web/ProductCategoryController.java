package com.cyecize.app.web;

import com.cyecize.app.api.product.ProductCategory;
import com.cyecize.app.api.product.ProductCategoryService;
import com.cyecize.app.api.product.dto.CreateCategoryDto;
import com.cyecize.app.api.product.dto.ProductCategoryDto;
import com.cyecize.app.constants.Endpoints;
import com.cyecize.app.constants.General;
import com.cyecize.app.error.NotFoundApiException;
import com.cyecize.http.HttpStatus;
import com.cyecize.summer.areas.security.annotations.PreAuthorize;
import com.cyecize.summer.areas.security.enums.AuthorizationType;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.DeleteMapping;
import com.cyecize.summer.common.annotations.routing.GetMapping;
import com.cyecize.summer.common.annotations.routing.PathVariable;
import com.cyecize.summer.common.annotations.routing.PostMapping;
import com.cyecize.summer.common.annotations.routing.PutMapping;
import com.cyecize.summer.common.annotations.routing.RequestMapping;
import com.cyecize.summer.common.models.JsonResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "", produces = General.APPLICATION_JSON)
@RequiredArgsConstructor
@PreAuthorize(role = General.ROLE_ADMIN)
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;
    private final ModelMapper modelMapper;

    @GetMapping(Endpoints.CATEGORIES)
    @PreAuthorize(AuthorizationType.ANY)
    public List<ProductCategoryDto> getCategories() {
        return this.productCategoryService.findAllCategories().stream()
                .map(productCategory -> this.modelMapper.map(productCategory, ProductCategoryDto.class))
                .collect(Collectors.toList());
    }

    @PostMapping(Endpoints.CATEGORIES)
    public ProductCategoryDto createCategory(@Valid CreateCategoryDto dto) {
        return this.modelMapper.map(this.productCategoryService.createCategory(dto), ProductCategoryDto.class);
    }

    @PutMapping(Endpoints.CATEGORY)
    public ProductCategoryDto editCategory(@PathVariable("id") Long catId, @Valid CreateCategoryDto dto) {
        return this.modelMapper.map(this.productCategoryService.editCategory(catId, dto), ProductCategoryDto.class);
    }

    @DeleteMapping(Endpoints.CATEGORY)
    public JsonResponse removeCategory(@PathVariable("id") Long catId) {
        this.productCategoryService.removeCategory(catId);

        return new JsonResponse(HttpStatus.OK)
                .addAttribute("message", "Category removed successfully!");
    }

    @GetMapping(Endpoints.CATEGORY)
    @PreAuthorize(AuthorizationType.ANY)
    public ProductCategoryDto getCategory(@PathVariable("id") Long catId) {
        final ProductCategory category = this.productCategoryService.getCategory(catId);
        if (category == null) {
            throw new NotFoundApiException(String.format("Category %d not found", catId));
        }

        return this.modelMapper.map(category, ProductCategoryDto.class);
    }
}
