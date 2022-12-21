package com.cyecize.app.util;

import com.cyecize.app.constants.Endpoints;
import com.cyecize.app.constants.General;
import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.summer.areas.template.annotations.TemplateService;
import com.cyecize.summer.common.annotations.Configuration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;

@TemplateService(serviceNameInTemplate = "util")
@RequiredArgsConstructor
public class TwigUtil {

    @Configuration("website.currency.name")
    private final String currency;

    @Configuration("website.scheme")
    private final String scheme;

    @Configuration("website.order.edit.url")
    private final String editOrderUrl;

    @Configuration("website.name")
    private final String websiteName;

    public String getWebsiteName() {
        return this.websiteName;
    }

    public String formatDate(LocalDateTime date) {
        return date.format(General.DEFAULT_DATE_TIME_FORMAT);
    }

    public String calculatePrice(Double d1, Integer qty) {
        return this.formatNumber(MathUtil.calculatePrice(d1, qty));
    }

    public String formatNumber(Double number) {
        return String.format("%.2f %s", number, this.currency);
    }

    public String createImageUrl(HttpSoletRequest request, String imagePath) {
        return String.format("%s://%s%s", this.scheme, request.getHost(), imagePath);
    }

    public String createProductUrl(HttpSoletRequest request, Long productId) {
        return String.format(
                "%s://%s%s",
                this.scheme,
                request.getHost(),
                Endpoints.PROD_FE.replace("{id}", productId + "")
        );
    }

    public String getEditOrderAddress(HttpSoletRequest request, Long orderId) {
        return String.format(
                "%s://%s%s",
                this.scheme,
                request.getHost(),
                this.editOrderUrl.replace("{id}", orderId + "")
        );
    }

    public boolean isNotNull(Object object) {
        return object != null;
    }
}
