package com.cyecize.app.web;

import com.cyecize.app.api.product.ProductCategory;
import com.cyecize.app.api.product.converter.CategoryIdDataAdapter;
import com.cyecize.app.api.product.productspec.CreateSpecificationTypeDto;
import com.cyecize.app.api.product.productspec.ProductSpecificationDto;
import com.cyecize.app.api.product.productspec.ProductSpecificationQuery;
import com.cyecize.app.api.product.productspec.ProductSpecificationService;
import com.cyecize.app.api.product.productspec.SpecificationType;
import com.cyecize.app.api.product.productspec.SpecificationTypeDto;
import com.cyecize.app.api.product.productspec.SpecificationTypeIdDataAdapter;
import com.cyecize.app.api.product.productspec.SpecificationTypeQuery;
import com.cyecize.app.api.product.productspec.SpecificationTypeService;
import com.cyecize.app.constants.Endpoints;
import com.cyecize.app.constants.General;
import com.cyecize.app.util.Page;
import com.cyecize.http.HttpStatus;
import com.cyecize.summer.areas.security.annotations.PreAuthorize;
import com.cyecize.summer.areas.security.enums.AuthorizationType;
import com.cyecize.summer.areas.validation.annotations.ConvertedBy;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.DeleteMapping;
import com.cyecize.summer.common.annotations.routing.PathVariable;
import com.cyecize.summer.common.annotations.routing.PostMapping;
import com.cyecize.summer.common.annotations.routing.PutMapping;
import com.cyecize.summer.common.annotations.routing.RequestMapping;
import com.cyecize.summer.common.models.JsonResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@PreAuthorize(role = General.ROLE_ADMIN)
@RequestMapping(value = "", produces = General.APPLICATION_JSON)
@RequiredArgsConstructor
public class SpecificationController {

    private final SpecificationTypeService specificationTypeService;

    private final ProductSpecificationService productSpecificationService;

    private final ModelMapper modelMapper;

    @PostMapping(Endpoints.SPECIFICATION_TYPES_SEARCH)
    @PreAuthorize(AuthorizationType.ANY)
    public Page<SpecificationTypeDto> searchSpecificationTypes(@Valid SpecificationTypeQuery query) {
        return this.specificationTypeService.findAll(query)
                .map(st -> this.modelMapper.map(st, SpecificationTypeDto.class));
    }

    @PostMapping(Endpoints.PRODUCT_SPECS_SEARCH)
    @PreAuthorize(AuthorizationType.ANY)
    public Map<Long, List<ProductSpecificationDto>> searchProductSpecifications(
            @Valid ProductSpecificationQuery query) {
        return this.productSpecificationService.findAllSpecifications(query)
                .stream()
                .map(spec -> this.modelMapper.map(spec, ProductSpecificationDto.class))
                .collect(Collectors.groupingBy(ProductSpecificationDto::getSpecificationTypeId));
    }

    @PutMapping(Endpoints.SPECIFICATION_CATEGORY)
    public JsonResponse addSpecificationToCategory(@ConvertedBy(SpecificationTypeIdDataAdapter.class)
                                                   @PathVariable("specTypeId")
                                                           SpecificationType specificationType,
                                                   @ConvertedBy(CategoryIdDataAdapter.class) @PathVariable("catId")
                                                           ProductCategory productCategory) {
        this.specificationTypeService.addCategory(specificationType, productCategory);
        return new JsonResponse(HttpStatus.OK).addAttribute("message", "Specification added successfully!");
    }

    @DeleteMapping(Endpoints.SPECIFICATION_CATEGORY)
    public JsonResponse removeSpecificationFromCategory(@ConvertedBy(SpecificationTypeIdDataAdapter.class)
                                                        @PathVariable("specTypeId")
                                                                SpecificationType specificationType,
                                                        @ConvertedBy(CategoryIdDataAdapter.class) @PathVariable("catId")
                                                                ProductCategory productCategory) {
        this.specificationTypeService.removeCategory(specificationType, productCategory);
        return new JsonResponse(HttpStatus.OK).addAttribute("message", "Specification removed successfully!");
    }

    @PostMapping(Endpoints.SPECIFICATION_TYPES)
    public SpecificationTypeDto createSpecificationType(@Valid CreateSpecificationTypeDto dto) {
        return this.modelMapper.map(this.specificationTypeService.createSpecificationType(dto), SpecificationTypeDto.class);
    }
}
