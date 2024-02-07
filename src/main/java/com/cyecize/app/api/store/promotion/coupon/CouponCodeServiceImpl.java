package com.cyecize.app.api.store.promotion.coupon;

import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.app.error.ApiException;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Configuration;
import com.cyecize.summer.common.annotations.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
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

    @Override
    public void deleteAllDisabledCodes() {
        this.couponCodeRepository.deleteByEnabledFalse();
    }

    @Override
    @Transactional
    public boolean useCouponCode(String code) {
        return this.getAndValidateCouponCode(code).map(couponCode -> {
            couponCode.setCurrentUsages(couponCode.getCurrentUsages() + 1);
            couponCode.setEnabled(isValidCouponCode(couponCode));

            this.couponCodeRepository.merge(couponCode);

            log.info("Used coupon code {}", code);
            return true;
        }).orElse(false);
    }

    @Override
    @Transactional(requiresNew = true)
    public CouponCodeDto getValidCouponCode(String code) {
        return this.getAndValidateCouponCode(code)
                .map(couponCode -> this.modelMapper.map(couponCode, CouponCodeDto.class))
                .orElse(null);
    }

    private Optional<CouponCode> getAndValidateCouponCode(String code) {
        final CouponCode couponCode = this.couponCodeRepository.findByCodeEnabled(code);

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

        final List<CouponCode> codesToSave = this.createReplicas(
                templateCouponCode, dto.getNumberOfCopies()
        );

        this.ensureUniqueCodes(codesToSave, 1);

        codesToSave.forEach(this.couponCodeRepository::persist);

        return codesToSave.stream()
                .map(couponCode -> this.modelMapper.map(couponCode, CouponCodeDto.class))
                .collect(Collectors.toList());
    }

    private void ensureUniqueCodes(List<CouponCode> codes, int attempt) {
        final Map<String, CouponCode> codeMap = codes.stream()
                .collect(Collectors.toMap(CouponCode::getCode, couponCode -> couponCode));

        final List<String> existingCodes = this.couponCodeRepository
                .selectCodeFindByCodeContaining(codeMap.keySet());

        if (existingCodes.isEmpty()) {
            return;
        }

        final List<CouponCode> fixedCodes = new ArrayList<>(existingCodes.size());
        for (String existingCode : existingCodes) {
            final CouponCode couponCode = codeMap.get(existingCode);
            couponCode.setCode(this.randomizeCode(existingCode, attempt));
            fixedCodes.add(couponCode);
        }

        this.ensureUniqueCodes(fixedCodes, attempt + 1);
    }

    private List<CouponCode> createReplicas(CouponCode template, int copies) {
        final List<CouponCode> result = new ArrayList<>();

        String nextCode = template.getCode();
        for (int copyCount = 0; copyCount < copies; copyCount++) {
            final CouponCode newCode = ObjectUtils.clone(template);
            newCode.setCode(nextCode);
            result.add(newCode);

            nextCode = template.getCode() + (copyCount + 1);
        }

        return result;
    }

    private void setUpCouponCode(CouponCode couponCode) {
        couponCode.setMaxUsages(Objects.requireNonNullElse(
                couponCode.getCurrentUsages(), Integer.MAX_VALUE
        ));

        couponCode.setExpiryDate(Objects.requireNonNullElse(
                couponCode.getExpiryDate(), LocalDateTime.MAX
        ));

        couponCode.setCode(Objects.requireNonNullElse(
                couponCode.getCode(), this.generatePromoCode()
        ));

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

        final String randomChar = UUID.randomUUID().toString().substring(0, 1);
        return code.substring(0, code.length() - 1) + randomChar;
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
