package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.TrendlyneIpoRecord;
import org.example.dto.TrendlyneResponse;
import org.example.entity.AlertLog;
import org.example.entity.TrendlyneIpo;
import org.example.repository.AlertLogRepository;
import org.example.repository.TrendlyneIpoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class TrendlyneIpoService {

    private static final Logger log = LoggerFactory.getLogger(TrendlyneIpoService.class);

    private static final String URL = "https://trendlyne.com/ipo/api/listing-details/";

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("d MMM, yyyy");

    private final RestTemplate restTemplate;
    private final TrendlyneIpoRepository repository;
    private final AlertService alertService;
    private final AlertLogRepository alertLogRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${alert.threshold.trendlyne.qib}")
    private double qibAlertThreshold;

    @Value("${alert.tier1.hni}")
    private double tier1HniThreshold;

    @Value("${alert.tier1.qib}")
    private double tier1QibThreshold;

    @Value("${alert.tier1.retail}")
    private double tier1RetailThreshold;

    public TrendlyneIpoService(RestTemplateBuilder builder, TrendlyneIpoRepository repository,
                               AlertService alertService, AlertLogRepository alertLogRepository) {
        this.restTemplate = builder
                .defaultHeader("User-Agent", "Mozilla/5.0 (compatible; PersonalIpoTracker/1.0)")
                .build();
        this.repository = repository;
        this.alertService = alertService;
        this.alertLogRepository = alertLogRepository;
    }

    public void fetchAndSaveAll() {
        TrendlyneResponse response;
        try {
            response = restTemplate.getForObject(URL, TrendlyneResponse.class);
        } catch (Exception e) {
            log.error("Failed to fetch Trendlyne data: {}", e.getMessage());
            if (e.getCause() != null) {
                log.error("Root cause: {}", e.getCause().getMessage());
            }
            return;
        }

        if (response == null || response.getBody() == null) {
            log.warn("Empty response from Trendlyne");
            return;
        }

        int savedCount = 0;
        for (Map.Entry<String, Object> entry : response.getBody().entrySet()) {
            String category = entry.getKey();
            Object value = entry.getValue();

            if (!(value instanceof List<?> rawList)) {
                continue; // skip non-list entries like "post_page_link" (string) or "ipo_score_card" (object)
            }

            for (Object item : rawList) {
                TrendlyneIpoRecord record;
                try {
                    record = objectMapper.convertValue(item, TrendlyneIpoRecord.class);
                } catch (Exception e) {
                    log.warn("Skipping unparseable record in category {}: {}", category, e.getMessage());
                    continue;
                }

                TrendlyneIpo entity = mapToEntity(record, category);
                repository.save(entity);
                savedCount++;

                if (Boolean.TRUE.equals(entity.getIsOpenNow())) {
                    checkAndAlert(entity);
                    checkTier1HniAlert(entity);
                }
            }
        }
        log.info("Saved {} Trendlyne IPO records across all categories", savedCount);
    }

    private void checkTier1HniAlert(TrendlyneIpo entity) {
        if (entity.getHni() == null || entity.getQib() == null || entity.getRetail() == null) {
            return;
        }

        boolean qualifies = entity.getHni() > tier1HniThreshold
                && entity.getQib() > tier1QibThreshold
                && entity.getRetail() > tier1RetailThreshold;

        if (!qualifies) {
            return;
        }

        // Separate prefix so this alert has its own independent dedup,
        // separate from the regular QIB-only alert on the same IPO
        String alertKey = "tl-tier1-" + entity.getSlug();

        if (alertLogRepository.existsBySlug(alertKey)) {
            return; // already alerted for this tier, don't spam
        }

        String board = Boolean.TRUE.equals(entity.getIsSme()) ? "SME" : "Mainboard";
        String stockLine = entity.getCompanyName() + " (" + board + ")";

        String message = String.format(
                "%s%nTIER_1_HNI%nTIER_1_HNI%nTIER_1_HNI%n%nQIB: %.2fx%nHNI: %.2fx%nRetail: %.2fx%nBid Closes: %s%nMin Investment: ₹%.0f",
                stockLine, entity.getQib(), entity.getHni(), entity.getRetail(),
                entity.getBidEndDate(), entity.getApplicationAmountMin()
        );

        alertService.sendTelegram(message);

        AlertLog logEntry = new AlertLog();
        logEntry.setSlug(alertKey);
        logEntry.setAlertedAt(LocalDateTime.now());
        alertLogRepository.save(logEntry);
    }

    private void checkAndAlert(TrendlyneIpo entity) {
        if (entity.getQib() == null || entity.getQib() < qibAlertThreshold) {
            return;
        }

        // Prefix with "tl-" so this never collides with Chittorgarh's AlertLog entries,
        // even if two sources happen to use similar slugs
        String alertKey = "tl-" + entity.getSlug();

        if (alertLogRepository.existsBySlug(alertKey)) {
            return; // already alerted for this IPO, don't spam
        }

        String board = Boolean.TRUE.equals(entity.getIsSme()) ? "SME" : "Mainboard";
        String subject = "IPO Alert (Trendlyne): " + entity.getCompanyName() + " QIB crossed " + qibAlertThreshold + "x";
        String body = String.format(
                "%s (%s) subscription update:%nQIB: %.2fx%nHNI: %.2fx%nRetail: %.2fx%nStrength/Risk: %s/%s%nExchange: %s%nBid Closes: %s%nMin Investment: ₹%.0f",
                entity.getCompanyName(), board, entity.getQib(), entity.getHni(), entity.getRetail(),
                entity.getStrengthCount(), entity.getRiskCount(), entity.getExchangeFlags(),
                entity.getBidEndDate(), entity.getApplicationAmountMin()
        );

        alertService.sendTelegram(subject + "\n" + body);

        AlertLog logEntry = new AlertLog();
        logEntry.setSlug(alertKey);
        logEntry.setAlertedAt(LocalDateTime.now());
        alertLogRepository.save(logEntry);
    }

    private TrendlyneIpo mapToEntity(TrendlyneIpoRecord record, String category) {
        TrendlyneIpo entity = new TrendlyneIpo();
        entity.setIpoId(record.getIpoId());
        entity.setCompanyName(record.getCompanyName());
        entity.setSlug(record.getSlug());
        entity.setCategory(category);
        entity.setBidStartDate(safeParseDate(record.getBidStartDate()));
        entity.setBidEndDate(safeParseDate(record.getBidEndDate()));
        entity.setStrengthCount(record.getStrengthCount());
        entity.setRiskCount(record.getRiskCount());
        entity.setPriceRangeMin(record.getPriceRangeMin());
        entity.setPriceRangeMax(record.getPriceRangeMax());
        entity.setSubscriptionValue(record.getSubscriptionValue());
        entity.setSubscriptionText(record.getSubscriptionText());
        entity.setQib(record.getQib());
        entity.setHni(record.getHni());
        entity.setRetail(record.getRetail());
        entity.setLotSize(record.getLotSize());
        entity.setIsOpenNow(record.getIsOpenNow());
        entity.setIsSme(record.getIsSme());
        entity.setExchangeFlags(record.getExchangeFlags());
        entity.setIssueSize(record.getIssueSize());
        entity.setApplicationAmountMin(record.getApplicationAmountMin());
        entity.setIsin(record.getIsin());
        entity.setFetchedAt(LocalDateTime.now());
        return entity;
    }

    private LocalDate safeParseDate(String value) {
        try {
            return LocalDate.parse(value, DATE_FORMAT);
        } catch (Exception e) {
            return null;
        }
    }

    public List<TrendlyneIpoRecord> previewAlerts() {
        TrendlyneResponse response;
        try {
            response = restTemplate.getForObject(URL, TrendlyneResponse.class);
        } catch (Exception e) {
            log.error("Failed to fetch Trendlyne data: {}", e.getMessage());
            return List.of();
        }

        if (response == null || response.getBody() == null) {
            return List.of();
        }

        List<TrendlyneIpoRecord> matches = new java.util.ArrayList<>();

        for (Map.Entry<String, Object> entry : response.getBody().entrySet()) {
            Object value = entry.getValue();
            if (!(value instanceof List<?> rawList)) {
                continue;
            }

            for (Object item : rawList) {
                try {
                    TrendlyneIpoRecord record = objectMapper.convertValue(item, TrendlyneIpoRecord.class);

                    boolean isOpen = Boolean.TRUE.equals(record.getIsOpenNow());
                    boolean crossedThreshold = record.getQib() != null && record.getQib() >= qibAlertThreshold;

                    if (isOpen && crossedThreshold) {
                        matches.add(record);
                    }
                } catch (Exception ignored) {
                    // not a valid IPO record shape, skip silently for this preview
                }
            }
        }
        return matches;
    }
}
