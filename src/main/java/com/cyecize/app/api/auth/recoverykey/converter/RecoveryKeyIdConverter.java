package com.cyecize.app.api.auth.recoverykey.converter;

import com.cyecize.app.api.auth.recoverykey.RecoveryKey;
import com.cyecize.app.api.auth.recoverykey.RecoveryKeyService;
import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.summer.areas.validation.interfaces.DataAdapter;
import com.cyecize.summer.common.annotations.Component;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Component
@RequiredArgsConstructor
public class RecoveryKeyIdConverter implements DataAdapter<RecoveryKey> {

    private final RecoveryKeyService recoveryKeyService;

    @Override
    public RecoveryKey resolve(String key, HttpSoletRequest httpSoletRequest) {
        if (StringUtils.trimToNull(key) == null) {
            return null;
        }

        return this.recoveryKeyService.searchKey(key);
    }
}
