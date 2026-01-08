package com.cyecize.app.api.frontend.opengraph;

import java.util.Map;
import lombok.Data;

@Data
public class OpenGraphData {

    private final String title;
    private final Map<String, String> seoTags;
    private final Map<String, String> ogTags;
}
