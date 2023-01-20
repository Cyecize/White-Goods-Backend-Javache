package com.cyecize.app.api.frontend.sitemap;

import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.solet.HttpSoletResponse;

public interface SitemapService {
    void downloadSitemapXml(HttpSoletRequest request, HttpSoletResponse response);
}
