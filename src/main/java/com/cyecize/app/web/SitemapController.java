package com.cyecize.app.web;

import com.cyecize.app.api.frontend.sitemap.SitemapService;
import com.cyecize.app.constants.Endpoints;
import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.solet.HttpSoletResponse;
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.GetMapping;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SitemapController {

    private final SitemapService sitemapService;

    @GetMapping(value = Endpoints.SEO_SITEMAP, produces = "text/xml")
    public void downloadSitemapXmlFile(HttpSoletRequest request, HttpSoletResponse response) {
        this.sitemapService.downloadSitemapXml(request, response);
    }
}
