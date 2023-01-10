package com.cyecize.app.config;

import com.cyecize.javache.JavacheConfigValue;
import com.cyecize.javache.services.JavacheConfigService;
import com.cyecize.solet.SoletConstants;
import com.cyecize.summer.common.annotations.Bean;
import com.cyecize.summer.common.annotations.BeanConfig;
import com.cyecize.summer.common.annotations.Configuration;
import com.cyecize.summer.utils.PathUtils;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;

@BeanConfig
@RequiredArgsConstructor
public class ThymeleafBeanConfig {
    // TODO: update summer so that default template engine can be replaced with thymeleaf

    @Configuration(SoletConstants.SOLET_CONFIG_ASSETS_DIR)
    private final String assetsDir;

    @Configuration(SoletConstants.SOLET_CFG_WORKING_DIR)
    private final String workingDir;

    @Configuration(SoletConstants.SOLET_CONFIG_SERVER_CONFIG_SERVICE_KEY)
    private final JavacheConfigService serverCfg;

    @Bean
    public TemplateEngine templateEngine() {
        final FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setCheckExistence(true);
        // Use assets dir because the index.html file coming from the FE will be located there upon deployment.
        resolver.setPrefix(PathUtils.appendPath(this.assetsDir, ""));
        resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resolver.setTemplateMode(TemplateMode.HTML);

        final FileTemplateResolver resolver2 = new FileTemplateResolver();
        // Use webapps dir in case app is built with FE integrated in it.
        final String webappDir = PathUtils.appendPath(
                this.workingDir,
                this.serverCfg.getConfigParamString(JavacheConfigValue.APP_RESOURCES_DIR_NAME)
        );
        resolver2.setPrefix(PathUtils.appendPath(webappDir, ""));
        resolver2.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resolver2.setTemplateMode(TemplateMode.HTML);

        final TemplateEngine engine = new TemplateEngine();
        engine.addTemplateResolver(resolver);
        engine.addTemplateResolver(resolver2);

        return engine;
    }
}
