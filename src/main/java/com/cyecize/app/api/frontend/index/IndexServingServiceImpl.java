package com.cyecize.app.api.frontend.index;

import com.cyecize.app.constants.General;
import com.cyecize.app.error.ApiException;
import com.cyecize.http.HttpStatus;
import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.solet.HttpSoletResponse;
import com.cyecize.solet.SoletConstants;
import com.cyecize.summer.common.annotations.Configuration;
import com.cyecize.summer.common.annotations.PostConstruct;
import com.cyecize.summer.common.annotations.Service;
import com.cyecize.summer.utils.PathUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class IndexServingServiceImpl implements IndexServingService {

    @Configuration(SoletConstants.SOLET_CONFIG_ASSETS_DIR)
    private final String assetsDir;

    private final TemplateEngine templateEngine;

    private Path indexPath;

    @PostConstruct
    public void init() {
        this.indexPath = Path.of(PathUtils.appendPath(this.assetsDir, "/index.html"));
    }

    @Override
    public boolean serveIndexFile(HttpSoletRequest request,
            HttpSoletResponse response,
            Map<String, String> metaTags) {
        if (!Files.exists(this.indexPath)) {
            return false;
        }

        response.setStatusCode(HttpStatus.OK);

        final Context context = new Context();
        final List<Pair> tags = metaTags.entrySet().stream()
                .map(kvp -> new Pair(kvp.getKey(), kvp.getValue()))
                .collect(Collectors.toList());

        context.setVariable("metaTags", tags);
        if (request.getQueryParameters().containsKey(General.QUERY_PARAM_LANG)) {
            context.setVariable("lang", request.getQueryParam(General.QUERY_PARAM_LANG));
        }

        final byte[] result = this.templateEngine.process("index.html", context)
                .getBytes(StandardCharsets.UTF_8);

        response.addHeader("Content-Type", "text/html");
        response.addHeader("Content-Disposition", "inline;");
        response.addHeader("Content-Length", result.length + "");

        try {
            response.getOutputStream().write(result);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApiException(String.format(
                    "Could not serve index.html file for request %s!",
                    request.getRequestURI()
            ));
        }

        return true;
    }

    @Data
    static class Pair {

        public final String key;
        public final String value;
    }
}
