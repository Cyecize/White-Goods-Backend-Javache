package com.cyecize.app.config;

import com.cyecize.solet.SoletConstants;
import com.cyecize.summer.common.annotations.Bean;
import com.cyecize.summer.common.annotations.BeanConfig;
import com.cyecize.summer.common.annotations.Configuration;
import com.cyecize.summer.utils.PathUtils;
import java.nio.charset.StandardCharsets;
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

    @Bean
    public TemplateEngine templateEngine() {
        final FileTemplateResolver resolver = new FileTemplateResolver();
        // Use assets dir because the index.html file coming from the FE will be located there upon deployment.
        resolver.setPrefix(PathUtils.appendPath(this.assetsDir, ""));
        resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resolver.setTemplateMode(TemplateMode.HTML);

        final TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(resolver);

        return engine;
    }
}
