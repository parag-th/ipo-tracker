package org.example.controller;

import org.example.dto.TrendlyneIpoRecord;
import org.example.entity.TrendlyneIpo;
import org.example.repository.TrendlyneIpoRepository;
import org.example.service.TrendlyneIpoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trendlyne")
public class TrendlyneController {

    private final TrendlyneIpoRepository repository;
    private final TrendlyneIpoService service;

    public TrendlyneController(TrendlyneIpoRepository repository, TrendlyneIpoService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping("/live")
    public List<TrendlyneIpo> getLatest() {
        return repository.findTop100ByOrderByFetchedAtDesc();
    }

    @GetMapping("/preview-alerts")
    public List<TrendlyneIpoRecord> previewAlerts() {
        return service.previewAlerts();
    }

    @PostMapping("/refresh")
    public String triggerRefresh() {
        service.fetchAndSaveAll();
        return "Trendlyne refresh triggered";
    }
}