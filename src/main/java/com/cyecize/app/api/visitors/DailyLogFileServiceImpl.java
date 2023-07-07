package com.cyecize.app.api.visitors;

import com.cyecize.app.api.visitors.dailyvisitordata.VisitorHitsPerHour;
import com.cyecize.app.api.visitors.dailyvisitordata.VisitorHitsPerHourRepository;
import com.cyecize.app.api.visitors.dailyvisitordata.VisitorHitsPerIp;
import com.cyecize.app.api.visitors.dailyvisitordata.VisitorHitsPerIpRepository;
import com.cyecize.app.api.visitors.dailyvisitordata.VisitorHitsPerPage;
import com.cyecize.app.api.visitors.dailyvisitordata.VisitorHitsPerPageRepository;
import com.cyecize.app.api.visitors.dto.AnalyzedVisitorLogDto;
import com.cyecize.app.api.visitors.dto.DailyStatisticDto;
import com.cyecize.app.api.visitors.dto.MonthlyStatisticDto;
import com.cyecize.app.integration.transaction.TransactionExecutor;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.ioc.annotations.Service;
import com.cyecize.solet.SoletConstants;
import com.cyecize.summer.common.annotations.Configuration;
import com.cyecize.summer.utils.PathUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailyLogFileServiceImpl implements DailyLogFileService {

    private static final int DEFAULT_STARTING_OFFSET = 2;

    private static final String FILE_NAME_FORMAT = "visitors-%04d-%02d-%02d.log";

    private final VisitorLogAnalyzer visitorLogAnalyzer;

    private final DailyLogFileRepository dailyLogFileRepository;

    private final VisitorHitsPerHourRepository visitorHitsPerHourRepository;

    private final VisitorHitsPerIpRepository visitorHitsPerIpRepository;

    private final VisitorHitsPerPageRepository visitorHitsPerPageRepository;

    private final TransactionExecutor transactionExecutor;

    @Configuration("visitor.logs.relative.location")
    private final String logFileRelativeLocation;

    @Configuration(SoletConstants.SOLET_CFG_WORKING_DIR)
    private final String workingDir;

    private final ModelMapper modelMapper;

    @Override
    public void processDailyFiles() {
        log.info("Begin processing of daily visitor files.");

        LocalDate startingDate = this.getStartingDate().toLocalDate();
        while (startingDate.isBefore(LocalDate.now())) {
            final LocalDate currentDate = startingDate;
            startingDate = startingDate.plusDays(1);
            final String filename = String.format(
                    FILE_NAME_FORMAT,
                    currentDate.getYear(),
                    currentDate.getMonthValue(),
                    currentDate.getDayOfMonth()
            );

            log.info("Processing visitor log file '{}'.", filename);

            final String fullPath = this.getLogFileFullPath(filename);

            final File logFile = new File(fullPath);
            if (!logFile.exists()) {
                log.warn("Visitor log file '{}' does not exist.", fullPath);
                continue;
            }

            try (BufferedReader br = new BufferedReader(new FileReader(logFile))) {
                final AnalyzedVisitorLogDto visitorLogDto = this.visitorLogAnalyzer.processLogFile(
                        br, filename
                );

                this.transactionExecutor.execute(() -> {
                    this.saveDailyLogFile(visitorLogDto, currentDate, filename);
                    log.info("Processed visitor file '{}'.", filename);
                });
            } catch (IOException ex) {
                log.warn("Error processing visitor file '{}'.", fullPath);
            }
        }
    }

    private LocalDateTime getStartingDate() {
        final DailyLogFile latest = this.dailyLogFileRepository.getLatest();
        if (latest != null) {
            return latest.getDateProcessed().plusDays(1);
        }

        return LocalDateTime.now().minusDays(DEFAULT_STARTING_OFFSET);
    }

    private String getLogFileFullPath(String filename) {
        final String fullDir = PathUtils.appendPath(this.workingDir, this.logFileRelativeLocation);
        return PathUtils.appendPath(fullDir, filename);
    }

    private void saveDailyLogFile(AnalyzedVisitorLogDto visitorLogDto, LocalDate date,
            String fileName) {
        final DailyLogFile dailyLogFile = new DailyLogFile();
        dailyLogFile.setDateProcessed(constructDateTime(date));
        dailyLogFile.setUniqueVisitors(visitorLogDto.getUniqueVisitors());
        dailyLogFile.setFilename(fileName);

        this.dailyLogFileRepository.persist(dailyLogFile);

        // Save visitor hits per hour
        visitorLogDto.getHourlyVisits().entrySet().stream()
                .sorted(Comparator.comparingInt(Entry::getKey))
                .forEach(kvp -> {
                    final VisitorHitsPerHour hitsPerHour = new VisitorHitsPerHour();
                    hitsPerHour.setHits(kvp.getValue());
                    hitsPerHour.setHour(kvp.getKey());
                    hitsPerHour.setDailyLogFileId(dailyLogFile.getId());

                    this.visitorHitsPerHourRepository.persist(hitsPerHour);
                });

        // Save visitor hits per ip
        visitorLogDto.getVisitorFrequency().forEach((key, value) -> {
            final VisitorHitsPerIp hitsPerIp = new VisitorHitsPerIp();
            hitsPerIp.setHits(value);
            hitsPerIp.setIp(key);
            hitsPerIp.setDailyLogFileId(dailyLogFile.getId());

            this.visitorHitsPerIpRepository.persist(hitsPerIp);
        });

        // Save visitor hits per page
        visitorLogDto.getPageViews().forEach((key, value) -> {
            final VisitorHitsPerPage hitsPerPage = new VisitorHitsPerPage();
            hitsPerPage.setHits(value);
            hitsPerPage.setUrl(key);
            hitsPerPage.setDailyLogFileId(dailyLogFile.getId());

            this.visitorHitsPerPageRepository.persist(hitsPerPage);
        });
    }

    @Override
    @Transactional
    public MonthlyStatisticDto getVisitorStatistics(Integer year, Integer month) {
        final LocalDateTime startDate = constructDateTime(LocalDate.of(year, month, 1));
        final LocalDateTime endDate = startDate.plusMonths(1).minusDays(1);

        final List<DailyLogFile> dailyLogFiles = this.dailyLogFileRepository.searchByDateFetchAll(
                startDate, endDate
        );

        return new MonthlyStatisticDto(
                year,
                month,
                dailyLogFiles.stream()
                        .map(dlf -> this.modelMapper.map(dlf, DailyStatisticDto.class))
                        .collect(Collectors.toList())
        );
    }

    private static LocalDateTime constructDateTime(LocalDate localDate) {
        return LocalDateTime.of(localDate, LocalTime.MIN);
    }
}
