package com.cyecize.app.api.product.converter;

import com.cyecize.app.api.product.Product;
import com.cyecize.app.api.product.ProductService;
import com.cyecize.app.api.user.User;
import com.cyecize.app.error.NotFoundApiException;
import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.summer.areas.security.models.Principal;
import com.cyecize.summer.areas.validation.interfaces.DataAdapter;
import com.cyecize.summer.common.annotations.Component;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;

@Component
@RequiredArgsConstructor
public class ProductIdDataAdapter implements DataAdapter<Product> {

    private final ProductService productService;
    private final Principal principal;

    @Override
    public Product resolve(String paramName, HttpSoletRequest httpSoletRequest) {
        final long id = NumberUtils.toLong(httpSoletRequest.getBodyParam(paramName),
                Integer.MIN_VALUE);
        final Product product = this.productService.findProductById(id, (User) principal.getUser());

        if (product == null) {
            throw new NotFoundApiException(String.format("Product with id %d was not found!", id));
        }

        return product;
    }
}
