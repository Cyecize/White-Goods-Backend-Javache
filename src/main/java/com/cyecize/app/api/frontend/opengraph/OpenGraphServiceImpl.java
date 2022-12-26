package com.cyecize.app.api.frontend.opengraph;

import com.cyecize.app.api.product.Product;
import com.cyecize.app.constants.General;
import com.cyecize.ioc.annotations.Nullable;
import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.summer.common.annotations.Configuration;
import com.cyecize.summer.common.annotations.Service;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Service
@RequiredArgsConstructor
public class OpenGraphServiceImpl implements OpenGraphService {

    @Nullable
    @Configuration("facebook.app.id")
    private final String facebookAppId;

    @Configuration("website.logo.path")
    private final String logoPath;

    @Configuration("website.scheme")
    private final String websiteScheme;

    //TODO: Load JSON with translations from assets folder
    @Override
    public Map<String, String> getTags(HttpSoletRequest request) {
        final Map<String, String> result = new HashMap<>();
        result.put("og:type", "website");
        result.put("og:url", request.getRequestURI());
        result.put("og:title", "Healthy Hair Project");
        result.put("og:image", String.format(
                "%s://%s%s",
                this.websiteScheme,
                request.getHost(),
                this.logoPath
        ));


        final String lang = request.getQueryParam(General.QUERY_PARAM_LANG);
        if (StringUtils.trimToEmpty(lang).equalsIgnoreCase("bg")) {
            result.put("og:description", "Сайт за разни шампоани");
        } else {
            result.put("og:description", "Website for shampoo and stuff");
        }

        if (StringUtils.trimToNull(this.facebookAppId) != null) {
            result.put("fb:app_id", this.facebookAppId);
        }

        return result;
    }

    @Override
    public Map<String, String> getTags(HttpSoletRequest request, Product product) {
        final Map<String, String> result = this.getTags(request);
        result.put("og:type", "product");
        result.put("product:is_product_shareable", "1");
        result.put("og:title", product.getProductName());
        result.put("og:image", String.format(
                "%s://%s%s",
                this.websiteScheme,
                request.getHost(),
                product.getImageUrl()
        ));
//        result.put("og:image:width", "400");
//        result.put("og:image:height", "400");

        final String lang = request.getQueryParam(General.QUERY_PARAM_LANG);
        if (StringUtils.trimToEmpty(lang).equalsIgnoreCase("bg")) {
            result.put("og:description", product.getDescriptionBg());
            result.put("product:category", product.getCategory().getNameBg());
        } else {
            result.put("og:description", product.getDescriptionEn());
            result.put("product:category", product.getCategory().getNameEn());
        }

        return result;
    }
}
