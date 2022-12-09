package com.cyecize.app.api.product.converter;

import com.cyecize.app.api.product.Product;
import com.cyecize.app.api.product.ProductService;
import com.cyecize.app.error.NotFoundApiException;
import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.summer.areas.security.models.Principal;
import com.cyecize.summer.common.annotations.Autowired;
import com.cyecize.summer.common.annotations.Component;

@Component
public class ProductIdNoErrorDataAdapter extends ProductIdDataAdapter {

    @Autowired
    public ProductIdNoErrorDataAdapter(ProductService productService, Principal principal) {
        super(productService, principal);
    }

    @Override
    public Product resolve(String paramName, HttpSoletRequest httpSoletRequest) {
        try {
            return super.resolve(paramName, httpSoletRequest);
        } catch (NotFoundApiException ignored) {
            return null;
        }
    }
}
