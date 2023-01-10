package com.cyecize.app.api.frontend.index;

import com.cyecize.app.constants.General;
import com.cyecize.app.error.ApiException;
import com.cyecize.http.HttpStatus;
import com.cyecize.javache.JavacheConfigValue;
import com.cyecize.javache.services.JavacheConfigService;
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

    @Configuration(SoletConstants.SOLET_CFG_WORKING_DIR)
    private final String workingDir;

    @Configuration(SoletConstants.SOLET_CONFIG_SERVER_CONFIG_SERVICE_KEY)
    private final JavacheConfigService serverCfg;

    private final TemplateEngine templateEngine;

    private Path indexPath1;
    private Path indexPath2;

    @PostConstruct
    public void init() {
        this.indexPath1 = Path.of(PathUtils.appendPath(this.assetsDir, "/index.html"));

        final String path2 = PathUtils.appendPath(
                this.workingDir,
                this.serverCfg.getConfigParamString(JavacheConfigValue.APP_RESOURCES_DIR_NAME)
        );
        this.indexPath2 = Path.of(path2, "/index.html");
    }

    @Override
    public boolean serveIndexFile(HttpSoletRequest request,
            HttpSoletResponse response,
            Map<String, String> metaTags) {
        if (!Files.exists(this.indexPath1) && !Files.exists(this.indexPath2)) {
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
