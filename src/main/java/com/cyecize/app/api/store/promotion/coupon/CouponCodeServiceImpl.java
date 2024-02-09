package com.cyecize.app.api.store.promotion.coupon;

import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.app.error.ApiException;
import com.cyecize.app.integration.transaction.TransactionExecutor;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.app.util.Page;
import com.cyecize.app.util.Specification;
import com.cyecize.summer.common.annotations.Configuration;
import com.cyecize.summer.common.annotations.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.modelmapper.ModelMapper;

@Service
@Slf4j
@RequiredArgsConstructor
public class CouponCodeServiceImpl implements CouponCodeService {

    private final CouponCodeRepository couponCodeRepository;

    private final ModelMapper modelMapper;

    @Configuration("coupon.code.default.length")
    private final int couponCodeLength;

    @Configuration("coupon.code.randomizer.max.attempts")
    private final int couponCodeRandomizeMaxAttempts;

    @Configuration("coupon.code.default.validity.days")
    private final int defaultCodeValidityDays;

    private final TransactionExecutor transactionExecutor;

    @Override
    public void deleteAllDisabledCodes() {
        this.couponCodeRepository.deleteByEnabledFalse();
    }

    @Override
    @Transactional
    public boolean useCouponCode(String code) {
        final AtomicBoolean isValid = new AtomicBoolean(false);

        // Run getAndValidate in new transaction so that it will ensure code gets set to disabled
        // Otherwise the following exceptions will cause the transaction to roll back and
        // the code will not be updated.
        this.transactionExecutor.executeNew(() -> {
            isValid.set(this.getAndValidateCouponCode(code).isPresent());
        });

        if (!isValid.get()) {
            return false;
        }

        final CouponCode couponCode = this.couponCodeRepository.findByCodeEnabled(code);
        Objects.requireNonNull(code);
        couponCode.setCurrentUsages(couponCode.getCurrentUsages() + 1);
        couponCode.setEnabled(isValidCouponCode(couponCode));
        this.couponCodeRepository.merge(couponCode);

        log.info("Used coupon code {} for promotion {}.", code, couponCode.getPromotionId());

        return true;
    }

    @Override
    @Transactional(requiresNew = true)
    public Optional<CouponCodeDto> getValidCouponCode(String code) {
        return this.getAndValidateCouponCode(code)
                .map(couponCode -> this.modelMapper.map(couponCode, CouponCodeDto.class));
    }

    private Optional<CouponCode> getAndValidateCouponCode(String code) {
        final CouponCode couponCode = this.couponCodeRepository.findByCodeEnabled(code);

        if (couponCode == null) {
            return Optional.empty();
        }

        if (!isValidCouponCode(couponCode)) {
            couponCode.setEnabled(false);
            this.couponCodeRepository.merge(couponCode);
            return Optional.empty();
        }

        return Optional.of(couponCode);
    }

    @Override
    @Transactional
    public List<CouponCodeDto> createCouponCodes(CreateCouponCodeDto dto) {
        final CouponCode templateCouponCode = this.modelMapper.map(dto, CouponCode.class);
        this.setUpCouponCode(templateCouponCode);

        if (!isValidCouponCode(templateCouponCode)) {
            throw new ApiException(ValidationMessages.COUPON_CODE_PAYLOAD_INVALID);
        }

        final boolean generateNewCodesForReplicas = dto.getCode() == null;
        final List<CouponCode> codesToSave = this.createReplicas(
                templateCouponCode,
                dto.getNumberOfCopies(),
                generateNewCodesForReplicas
        );

        this.ensureUniqueCodes(codesToSave, 1);

        codesToSave.forEach(this.couponCodeRepository::persist);

        log.info("Created {} coupon codes for promotion {}.",
                dto.getNumberOfCopies(),
                dto.getPromotionId()
        );

        return codesToSave.stream()
                .map(couponCode -> this.modelMapper.map(couponCode, CouponCodeDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<CouponCode> search(CouponCodeQuery query) {
        final Specification<CouponCode> spec = CouponCodeSpecifications.sort(query.getSort())
                .and(CouponCodeSpecifications.containsCode(query.getCode()))
                .and(CouponCodeSpecifications.promotionIdEquals(query.getPromotionId()))
                .and(CouponCodeSpecifications.expiryDateBetween(query.getExpiryDate()))
                .and(CouponCodeSpecifications.enabled(query.getEnabled()));

        return this.couponCodeRepository.search(spec, query.getPage());
    }

    private void ensureUniqueCodes(List<CouponCode> codes, int attempt) {
        final Map<String, CouponCode> codeMap = codes.stream()
                .collect(Collectors.toMap(CouponCode::getCode, couponCode -> couponCode));

        final List<String> existingCodes = this.couponCodeRepository
                .selectCodeFindByCodeContaining(codeMap.keySet());

        if (existingCodes.isEmpty()) {
            return;
        }

        final Set<String> newCodes = new HashSet<>(existingCodes.size(), 1);
        for (String existingCode : existingCodes) {
            final CouponCode couponCode = codeMap.get(existingCode);

            String newCode = this.randomizeCode(existingCode, attempt);
            int tryCount = 0;
            while (codeMap.containsKey(newCode) || newCodes.contains(newCode)) {
                newCode = this.randomizeCode(existingCode, tryCount++);
            }

            couponCode.setCode(newCode);
            newCodes.add(newCode);
        }

        this.ensureUniqueCodes(codes, attempt + 1);
    }

    private List<CouponCode> createReplicas(CouponCode template,
            int copies,
            boolean generateNewCodes) {
        final List<CouponCode> result = new ArrayList<>();

        final Set<String> generatedCodes = new HashSet<>();
        String nextCode = template.getCode();
        for (int copyCount = 1; copyCount <= copies; copyCount++) {
            final CouponCode newCode = SerializationUtils.clone(template);
            newCode.setCode(nextCode);
            generatedCodes.add(nextCode);
            result.add(newCode);

            int codeGenAttempt = 0;
            do {
                if (generateNewCodes) {
                    nextCode = this.generatePromoCode();
                } else {
                    nextCode = template.getCode() + (copyCount + 1);
                }
                codeGenAttempt++;

                if (codeGenAttempt > this.couponCodeRandomizeMaxAttempts) {
                    throw new ApiException(ValidationMessages.COUPON_CODE_RANDOMIZE_FAILED);
                }
            }
            while (generatedCodes.contains(nextCode));
        }

        return result;
    }

    private void setUpCouponCode(CouponCode couponCode) {
        couponCode.setMaxUsages(Objects.requireNonNullElse(
                couponCode.getMaxUsages(), Integer.MAX_VALUE
        ));

        couponCode.setExpiryDate(Objects.requireNonNullElse(
                couponCode.getExpiryDate(),
                LocalDateTime.now().plusDays(this.defaultCodeValidityDays)
        ));

        couponCode.setCode(
                Objects.requireNonNullElse(
                        couponCode.getCode(), this.generatePromoCode()
                ).toUpperCase()
        );

        couponCode.setCreateDate(LocalDateTime.now());
        couponCode.setEnabled(true);
        couponCode.setCurrentUsages(0);
    }

    private String generatePromoCode() {
        return UUID.randomUUID().toString().substring(0, this.couponCodeLength).toUpperCase();
    }

    private String randomizeCode(String code, int attempt) {
        if (attempt > this.couponCodeRandomizeMaxAttempts) {
            throw new ApiException(ValidationMessages.COUPON_CODE_RANDOMIZE_FAILED);
        }

        final int charsToRandomize = code.length() < 2 ? 1 : 2;

        final String randomChar = UUID.randomUUID().toString().substring(0, charsToRandomize);
        return (code.substring(0, code.length() - charsToRandomize) + randomChar).toUpperCase();
    }

    private static boolean isValidCouponCode(CouponCode couponCode) {
        if (!couponCode.getEnabled()) {
            return false;
        }

        if (couponCode.getCurrentUsages() >= couponCode.getMaxUsages()) {
            return false;
        }

        if (couponCode.getExpiryDate().isBefore(LocalDateTime.now())) {
            return false;
        }

        return true;
    }
}
