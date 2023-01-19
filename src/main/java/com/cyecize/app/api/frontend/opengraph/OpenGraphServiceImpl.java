package com.cyecize.app.api.frontend.opengraph;

import com.cyecize.app.api.product.Product;
import com.cyecize.app.constants.General;
import com.cyecize.ioc.annotations.Nullable;
import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.summer.common.annotations.Configuration;
import com.cyecize.summer.common.annotations.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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

    @Configuration("website.name")
    private final String websiteName;

    @Configuration("website.default.lang")
    private final String defaultLang;

    @Configuration("website.description.bg")
    private final String descriptionBg;

    @Configuration("website.description.en")
    private final String descriptionEn;

    @Configuration("website.keywords.bg")
    private final String keywordsBg;

    @Configuration("website.keywords.en")
    private final String keywordsEn;

    @Configuration("website.currency.name")
    private final String currency;

    @Override
    public OpenGraphData getTags(HttpSoletRequest request) {
        final Map<String, String> result = new HashMap<>();
        result.put("og:type", "website");
        result.put("og:url", request.getRequestURI());

        final String title = this.websiteName;
        result.put("og:title", title);
        result.put("og:image", String.format(
                "%s://%s%s",
                this.websiteScheme,
                request.getHost(),
                this.logoPath
        ));
        result.put("og:site_name", title);
        result.put("twitter:card", "summary_large_image");
        result.put("twitter:title", title);

        final String lang = request.getQueryParam(General.QUERY_PARAM_LANG);
        final String description;
        if (StringUtils.trimToEmpty(lang).equalsIgnoreCase("bg")) {
            description = this.descriptionBg;
        } else {
            description = this.descriptionEn;
        }

        result.put("og:description", description);
        result.put("twitter:description", description);

        if (StringUtils.trimToNull(this.facebookAppId) != null) {
            result.put("fb:app_id", this.facebookAppId);
        }

        return new OpenGraphData(title, this.getSEOTags(request), result);
    }

    @Override
    public OpenGraphData getTags(HttpSoletRequest request, Product product) {
        final OpenGraphData pageData = this.getTags(request);
        final Map<String, String> result = pageData.getOgTags();
        result.put("og:type", "product");
        result.put("product:is_product_shareable", "1");

        final String title = String.format("%s - %s", product.getProductName(), this.websiteName);
        result.put("og:title", title);
        result.put("og:image", String.format(
                "%s://%s%s",
                this.websiteScheme,
                request.getHost(),
                product.getImageUrl()
        ));
//        result.put("og:image:width", "400");
//        result.put("og:image:height", "400");
        result.put("twitter:title", title);

        final String lang = this.getLang(request);
        final String description;
        if (StringUtils.trimToEmpty(lang).equalsIgnoreCase("bg")) {
            result.put("product:category", product.getCategory().getNameBg());
            description = product.getDescriptionBg();
        } else {
            description = product.getDescriptionEn();
            result.put("product:category", product.getCategory().getNameEn());
        }

        result.put("og:description", description);
        result.put("twitter:description", description);

        if (product.getPrice() != null) {
            result.put("og:price:amount", product.getPrice().toString());
            result.put("og:price:currency", this.currency);
        }

        return new OpenGraphData(title, pageData.getSeoTags(), result);
    }

    private Map<String, String> getSEOTags(HttpSoletRequest request) {
        final Map<String, String> result = new HashMap<>();
        final String lang = this.getLang(request);
        if (lang.equalsIgnoreCase("bg")) {
            result.put("description", this.descriptionBg);
            result.put("keywords", this.keywordsBg);
        } else {
            result.put("description", this.descriptionEn);
            result.put("keywords", this.keywordsEn);
        }

        return result;
    }

    private String getLang(HttpSoletRequest request) {
        return Objects.requireNonNullElse(
                StringUtils.trimToNull(request.getQueryParam(General.QUERY_PARAM_LANG)),
                this.defaultLang
        );
    }
}
