package com.cyecize.app.api.visitors;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LogEntry {

    private final String ip;
    private final LocalDateTime time;
    private final String route;
}
