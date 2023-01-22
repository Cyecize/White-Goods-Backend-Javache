package com.cyecize.app.api.frontend.sitemap;

import static com.cyecize.solet.SoletConstants.SOLET_CFG_WORKING_DIR;
import static com.cyecize.solet.SoletConstants.SOLET_CONFIG_SERVER_CONFIG_SERVICE_KEY;

import com.cyecize.app.api.product.ProductService;
import com.cyecize.app.error.ApiException;
import com.cyecize.app.error.NotFoundApiException;
import com.cyecize.app.util.TwigUtil;
import com.cyecize.ioc.annotations.Service;
import com.cyecize.javache.JavacheConfigValue;
import com.cyecize.javache.services.JavacheConfigService;
import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.solet.HttpSoletResponse;
import com.cyecize.summer.common.annotations.Configuration;
import com.cyecize.summer.utils.PathUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
public class SitemapServiceImpl implements SitemapService {

    private final String workingDir;

    private final JavacheConfigService serverCfg;

    private final TemplateEngine templateEngine;

    private final TwigUtil twigUtil;

    private final String scheme;

    private final ProductService productService;

    private final Path webappDir;

    public SitemapServiceImpl(
            @Configuration(SOLET_CFG_WORKING_DIR) String workingDir,
            @Configuration(SOLET_CONFIG_SERVER_CONFIG_SERVICE_KEY) JavacheConfigService serverCfg,
            TemplateEngine templateEngine,
            TwigUtil twigUtil,
            @Configuration("website.scheme") String scheme,
            ProductService productService) {
        this.workingDir = workingDir;
        this.serverCfg = serverCfg;
        this.templateEngine = templateEngine;
        this.twigUtil = twigUtil;
        this.scheme = scheme;
        this.productService = productService;
        this.webappDir = this.getWebappDir();
    }

    @Override
    public void downloadSitemapXml(HttpSoletRequest request, HttpSoletResponse response) {
        if (!Files.exists(this.webappDir)) {
            log.error("Missing sitemap.xml file in {}.", this.webappDir);
            throw new NotFoundApiException("sitemap.xml not found!");
        }

        final Context context = new Context();
        context.setVariable(
                "host",
                String.format("%s://%s", this.scheme, request.getHost())
        );

        final var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        final List<SitemapEntryDto> urls = this.productService.findAllForSitemap()
                .stream()
                .map(prod -> new SitemapEntryDto(
                        this.twigUtil.createProductUrl(request, prod.getId()),
                        prod.getLastUpdated().format(formatter),
                        "Daily",
                        "1"
                ))
                .collect(Collectors.toList());

        context.setVariable("additionalUrls", urls);

        final byte[] result = this.templateEngine
                .process("assets/seo/sitemap.xml", context)
                .getBytes(StandardCharsets.UTF_8);

        response.addHeader("Content-Type", "text/xml");
        response.addHeader("Content-Disposition", "inline;");
        response.addHeader("Content-Length", result.length + "");

        try {
            response.getOutputStream().write(result);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApiException("Could not serve sitemap.xml file!");
        }

        log.info("SEO file sitemap.xml was requested!");
    }

    private Path getWebappDir() {
        final String webappDir = PathUtils.appendPath(
                this.workingDir,
                this.serverCfg.getConfigParamString(JavacheConfigValue.APP_RESOURCES_DIR_NAME)
        );

        return Path.of(webappDir);
    }
}
