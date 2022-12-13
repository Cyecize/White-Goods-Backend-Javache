package com.cyecize.app.api.translate;

import com.cyecize.solet.SoletConstants;
import com.cyecize.summer.areas.template.annotations.TemplateService;
import com.cyecize.summer.common.annotations.Configuration;
import com.cyecize.summer.common.annotations.PostConstruct;
import com.cyecize.summer.utils.PathUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;


/**
 * If needed, add support for multiple languages. For now single language is enough.
 */
@TemplateService(serviceNameInTemplate = "translate")
@RequiredArgsConstructor
public class TranslateServiceImpl implements TranslateService {

    private static final String I18N_DIR = "i18n/";

    @Configuration(SoletConstants.SOLET_CFG_WORKING_DIR)
    private final String workingDir;

    @Configuration("website.default.lang")
    private final String defaultLang;

    private final ObjectMapper objectMapper;

    private Map<String, String> dict;

    @PostConstruct
    void init() throws IOException {
        final TypeReference<HashMap<String, String>> typeRef = new TypeReference<>() {
        };

        this.dict = this.objectMapper.readValue(
                new File(PathUtils.appendPath(
                        this.workingDir,
                        I18N_DIR + this.defaultLang + ".json")
                ),
                typeRef
        );
    }

    @Override
    public String getValue(String key) {
        return this.dict.getOrDefault(key, key);
    }
}
