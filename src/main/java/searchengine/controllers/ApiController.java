package searchengine.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import searchengine.dto.statistics.ErrorResponse;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.dto.statistics.SuccessResponse;
import searchengine.services.IndexingService;
import searchengine.services.StatisticsService;

@RestController
@RequestMapping("/api")
public class ApiController {

    private boolean isIndexingRunning = false;

    private final StatisticsService statisticsService;
    private final IndexingService indexingService;

    @Autowired
    public ApiController(StatisticsService statisticsService, IndexingService indexingService) {
        this.statisticsService = statisticsService;
        this.indexingService = indexingService;
    }

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }

    @GetMapping("/startIndexing")
    public ResponseEntity<Object> startIndexing() throws InterruptedException {
        if (!isIndexingRunning) {
            indexingService.indexSites();
            return ResponseEntity.ok(new SuccessResponse());
        } else {
            ErrorResponse errorResponse = new ErrorResponse("Индексация уже запущена");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
