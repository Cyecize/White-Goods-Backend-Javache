package com.cyecize.app.api.auth.recoverykey;

import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Configuration;
import com.cyecize.summer.common.annotations.Service;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecoveryKeyServiceImpl implements RecoveryKeyService {

    private final RecoveryKeyRepository recoveryKeyRepository;

    @Configuration("recovery.key.valid.minutes")
    private final int validMinutes;

    @Override
    public RecoveryKey createRecoveryKey(Long userId) {
        final RecoveryKey key = new RecoveryKey();
        key.setDateCreated(LocalDateTime.now());
        key.setUserId(userId);

        return this.recoveryKeyRepository.persist(key);
    }

    @Override
    @Transactional
    public RecoveryKey searchKey(String key) {
        final RecoveryKey recoveryKey = this.recoveryKeyRepository.findById(key);
        if (recoveryKey == null) {
            return null;
        }

        final LocalDateTime validUntil = LocalDateTime.now().plusMinutes(this.validMinutes);
        if (recoveryKey.getDateCreated().isAfter(validUntil)) {
            this.destroyKey(key);
            return null;
        }

        return recoveryKey;
    }

    @Override
    public void destroyKey(String key) {
        this.recoveryKeyRepository.deleteById(key);
    }

    @Override
    public void destroyKeys(Long userId) {
        this.recoveryKeyRepository.deleteByUserId(userId);
    }
}
