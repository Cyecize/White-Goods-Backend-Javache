package com.cyecize.app.api.frontend.index;

import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.solet.HttpSoletResponse;
import java.util.Map;

public interface IndexServingService {

    boolean serveIndexFile(HttpSoletRequest request,
            HttpSoletResponse response,
            Map<String, String> metaTags
    );
}
