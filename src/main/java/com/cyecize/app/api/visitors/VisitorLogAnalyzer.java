package com.cyecize.app.api.visitors;

import com.cyecize.app.api.visitors.dto.AnalyzedVisitorLogDto;
import java.io.BufferedReader;
import java.io.IOException;

public interface VisitorLogAnalyzer {

    AnalyzedVisitorLogDto processLogFile(BufferedReader br, String filename) throws IOException;
}
