package com.cyecize.app.api.product.selection;

import java.util.List;

public interface ProductSelectionService {

    List<ProductSelection> getAll(boolean enabledOnly);

    ProductSelection save(CreateProductSelectionDto dto);

    ProductSelection get(Long id);

    void remove(Long id);
}
