package com.cyecize.app.api.auth.recoverykey;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class RecoveryKeyDto {

    private String id;

    private Long userId;

    private LocalDateTime dateCreated;
}
