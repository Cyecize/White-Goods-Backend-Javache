package com.cyecize.app.api.auth.recoverykey;

public interface RecoveryKeyService {

    RecoveryKey createRecoveryKey(Long userId);

    RecoveryKey searchKey(String key);

    void destroyKey(String key);

    void destroyKeys(Long userId);
}
