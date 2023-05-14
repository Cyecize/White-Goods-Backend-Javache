package com.cyecize.app.api.visitors;

import com.sun.istack.Nullable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class VisitorLogUtils {

    private static final String LOG_LINE_SEPARATOR = "\\s+\\|\\s+";
    private static final DateTimeFormatter LOG_DATE_FORMAT = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm:ss");


    public static @Nullable LogEntry parseLine(String line) {
        if (StringUtils.trimToNull(line) == null) {
            log.warn("Trying to process empty visitor line.");
            return null;
        }

        final String[] lineComponents = line.split(LOG_LINE_SEPARATOR);
        if (lineComponents.length != 3) {
            log.warn("Invalid visitor log format, '{}'.", line);
            return null;
        }

        final LocalDateTime time;
        try {
            time = LocalDateTime.parse(lineComponents[0], LOG_DATE_FORMAT);
        } catch (DateTimeParseException ex) {
            log.warn("Invalid date format for visitor line '{}'.", line);
            return null;
        }
        final String route = lineComponents[1];
        final String ip = lineComponents[2];

        return new LogEntry(ip, time, route);
    }
}
