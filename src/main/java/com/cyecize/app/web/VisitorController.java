package com.cyecize.app.web;

import com.cyecize.app.api.visitors.DailyLogFileService;
import com.cyecize.app.api.visitors.dto.MonthlyStatisticDto;
import com.cyecize.app.constants.Endpoints;
import com.cyecize.app.constants.General;
import com.cyecize.http.HttpStatus;
import com.cyecize.summer.areas.security.annotations.PreAuthorize;
import com.cyecize.summer.areas.security.enums.AuthorizationType;
import com.cyecize.summer.common.annotations.Configuration;
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.GetMapping;
import com.cyecize.summer.common.annotations.routing.PathVariable;
import com.cyecize.summer.common.annotations.routing.PostMapping;
import com.cyecize.summer.common.annotations.routing.RequestParam;
import com.cyecize.summer.common.models.JsonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@PreAuthorize(role = General.ROLE_ADMIN)
@RequiredArgsConstructor
public class VisitorController {

    @Configuration("visitors.process.pwd")
    private final String visitorPassword;

    private final DailyLogFileService dailyLogFileService;

    @PreAuthorize(AuthorizationType.ANY)
    @PostMapping(Endpoints.PROCESS_VISITORS)
    public JsonResponse processVisitorFiles(@RequestParam("pwd") String pwd) {
        if (!visitorPassword.equals(pwd)) {
            log.warn("Cannot process visitor files, wrong password!");
            return new JsonResponse(HttpStatus.UNAUTHORIZED)
                    .addAttribute("message", "Wrong password");
        }

        this.dailyLogFileService.processDailyFiles();
        return new JsonResponse(HttpStatus.OK)
                .addAttribute("message", "Files processed");
    }

    @GetMapping(Endpoints.VISITORS_MONTH)
    public MonthlyStatisticDto getMonthlyVisitorData(@PathVariable("year") Integer year,
            @PathVariable("month") Integer month) {
        return this.dailyLogFileService.getVisitorStatistics(year, month);
    }
}
