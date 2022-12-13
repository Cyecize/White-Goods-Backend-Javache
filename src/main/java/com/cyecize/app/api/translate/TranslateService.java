package com.cyecize.app.api.translate;

public interface TranslateService {

    /**
     * @param key - translate key
     * @return word / phrase under that key or the actual key of nothing is present.
     */
    String getValue(String key);
}
