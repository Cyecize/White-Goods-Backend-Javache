package com.cyecize.app.api.visitors;

import com.cyecize.app.api.visitors.dto.AnalyzedVisitorLogDto;
import com.cyecize.ioc.annotations.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class VisitorLogAnalyzerImpl implements VisitorLogAnalyzer {

    @Override
    public AnalyzedVisitorLogDto processLogFile(
            BufferedReader br,
            String filename) throws IOException {
        final Map<String, Integer> pageViews = new HashMap<>();
        final Map<String, Integer> visitorFrequency = new HashMap<>();
        final Map<Integer, Integer> hourlyVisits = new HashMap<>();
        int uniqueVisitors = 0;

        String line;
        while ((line = br.readLine()) != null) {
            final LogEntry entry = VisitorLogUtils.parseLine(line);
            if (entry == null) {
                log.warn("Invalid line found in file '{}', skipping.", filename);
                continue;
            }

            final int visitorCount = visitorFrequency.getOrDefault(entry.getIp(), 0);

            // Update unique visitors
            if (visitorCount <= 0) {
                uniqueVisitors++;
            }

            // Update visitor frequency
            visitorFrequency.put(entry.getIp(), visitorCount + 1);

            // Update page views
            pageViews.put(entry.getRoute(), pageViews.getOrDefault(entry.getRoute(), 0) + 1);

            // Update hourly visits
            hourlyVisits.put(entry.getTime().getHour(),
                    hourlyVisits.getOrDefault(entry.getTime().getHour(), 0) + 1);
        }

        return new AnalyzedVisitorLogDto(pageViews, visitorFrequency, hourlyVisits, uniqueVisitors);
    }
}
