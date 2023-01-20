package com.cyecize.app.api.frontend.sitemap;

import lombok.Data;

@Data
public class SitemapEntryDto {

    private final String url;
    private final String date;
    private final String changeFreq;
    private final String priority;
}
