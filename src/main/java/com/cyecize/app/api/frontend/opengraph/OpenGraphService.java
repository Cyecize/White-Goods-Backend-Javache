package com.cyecize.app.api.frontend.opengraph;

import com.cyecize.app.api.product.Product;
import com.cyecize.solet.HttpSoletRequest;
import java.util.Map;

public interface OpenGraphService {

    OpenGraphData getTags(HttpSoletRequest request);

    OpenGraphData getTags(HttpSoletRequest request, Product product);
}
