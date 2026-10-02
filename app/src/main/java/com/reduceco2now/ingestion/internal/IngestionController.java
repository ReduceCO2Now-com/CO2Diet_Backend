package com.reduceco2now.ingestion.internal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ingestion")
public class IngestionController {

    private final IngestionService ingestionService;

    public IngestionController(IngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping("/run")
    public ResponseEntity<String> triggerIngestion() {
        ingestionService.runIngestion();
        return ResponseEntity.ok("Ingestion run successfully completed. Check server logs for details.");
    }
}