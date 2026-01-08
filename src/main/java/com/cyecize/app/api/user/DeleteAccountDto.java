package com.cyecize.app.api.user;

import com.cyecize.summer.areas.validation.constraints.BooleanExactValue;
import lombok.Data;

@Data
public class DeleteAccountDto {

    @BooleanExactValue(true)
    private Boolean confirmDeleteAccount;
}
