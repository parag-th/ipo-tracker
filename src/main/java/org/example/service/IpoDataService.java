package org.example.service;

import org.example.dto.ChittorgarhResponse;
import org.example.dto.IpoRecord;
import org.example.entity.AlertLog;
import org.example.entity.IpoSubscription;
import org.example.entity.TrendlyneIpo;
import org.example.repository.AlertLogRepository;
import org.example.repository.IpoSubscriptionRepository;
import org.example.repository.TrendlyneIpoRepository;
import org.jsoup.Jsoup;
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

@Service
public class IpoDataService {

    private static final Logger log = LoggerFactory.getLogger(IpoDataService.class);

    private static final String BASE_URL =
            "https://webnodejs.chittorgarh.com/cloud/report/data-read/21/1/7/%d/%s/0/%s/0?search=&v=11-05";

    private static final DateTimeFormatter SUBSCRIPTION_TS_FORMAT =
            DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm");

    private final RestTemplate restTemplate;
    private final IpoSubscriptionRepository repository;

//    ---------NEW FIELDS
    private final AlertService alertService;
    private final AlertLogRepository alertLogRepository;
    private final TrendlyneIpoRepository trendlyneIpoRepository;

    @Value("${ipo.tracker.category:mainboard}")
    private String category;

    @Value("${alert.threshold.total}")
    private double alertThreshold;

    public IpoDataService(RestTemplateBuilder builder, IpoSubscriptionRepository repository,
                          AlertService alertService, AlertLogRepository alertLogRepository,
                          TrendlyneIpoRepository trendlyneIpoRepository) {
        this.restTemplate = builder
                .defaultHeader("User-Agent", "Mozilla/5.0 (compatible; PersonalIpoTracker/1.0)")
                .build();
        this.repository = repository;
        this.alertService = alertService;
        this.alertLogRepository = alertLogRepository;
        this.trendlyneIpoRepository = trendlyneIpoRepository;
    }

    public void fetchAndSaveOpenIpos() {
        String url = buildUrl();
        log.info("Fetching IPO data from {}", url);

        ChittorgarhResponse response;
        try {
            response = restTemplate.getForObject(url, ChittorgarhResponse.class);
        } catch (Exception e) {
            log.error("Failed to fetch IPO data: {}", e.getMessage());
            return;
        }

        if (response == null || response.getReportTableData() == null) {
            log.warn("Empty response from Chittorgarh");
            return;
        }

        LocalDate today = LocalDate.now();
        int savedCount = 0;

        for (IpoRecord record : response.getReportTableData()) {
            if (!isCurrentlyOpen(record, today)) {
                continue; // skip closed/historic IPOs, we only want the live ones
            }

            IpoSubscription entity = mapToEntity(record);
            repository.save(entity);

            savedCount++;

            checkAndAlert(entity);
        }
        log.info("Saved {} open IPO subscription snapshots", savedCount);
    }
    
//    -----------Adding new method-------
private void checkAndAlert(IpoSubscription entity) {
    if (entity.getTotal() == null || entity.getTotal() < alertThreshold) {
        return;
    }
    if (alertLogRepository.existsBySlug(entity.getSlug())) {
        return;
    }

    String minInvestment = "N/A";
    String targetName = normalizeCompanyName(entity.getCompanyName());

    Double amount = trendlyneIpoRepository.findTop100ByOrderByFetchedAtDesc().stream()
            .filter(t -> normalizeCompanyName(t.getCompanyName()).equals(targetName))
            .map(TrendlyneIpo::getApplicationAmountMin)
            .filter(a -> a != null)
            .findFirst()
            .orElse(null);

    if (amount != null) {
        minInvestment = String.format("₹%.0f", amount);
    }

    String subject = "IPO Alert: " + entity.getCompanyName() + " crossed " + alertThreshold + "x";
    String body = String.format(
            "%s subscription update:%nQIB: %.2fx%nRetail: %.2fx%nTotal: %.2fx%nAs on: %s%nBid Closes: %s%nMin Investment: %s",
            entity.getCompanyName(), entity.getQib(), entity.getRetail(), entity.getTotal(),
            entity.getSubscriptionAsOn(), entity.getClosingDate(), minInvestment
    );

    alertService.sendTelegram(subject + "\n" + body);

    AlertLog logEntry = new AlertLog();
    logEntry.setSlug(entity.getSlug());
    logEntry.setAlertedAt(LocalDateTime.now());
    alertLogRepository.save(logEntry);
}
    
//    ----------NEW SECTION END----------

    private boolean isCurrentlyOpen(IpoRecord record, LocalDate today) {
        try {
            LocalDate openDate = LocalDate.parse(record.getIssueOpenDate());
            LocalDate closeDate = LocalDate.parse(record.getIssueCloseDate());
            return !today.isBefore(openDate) && !today.isAfter(closeDate);
        } catch (Exception e) {
            return false;
        }
    }

    private IpoSubscription mapToEntity(IpoRecord record) {
        IpoSubscription entity = new IpoSubscription();
        entity.setChittorgarhId(record.getId());
        entity.setCompanyName(cleanCompanyName(record.getCompanyHtml()));
        entity.setSlug(record.getSlug());
        entity.setOpeningDate(safeParseDate(record.getIssueOpenDate()));
        entity.setClosingDate(safeParseDate(record.getIssueCloseDate()));
        entity.setIsin(record.getIsin());
        entity.setQib(record.getQib());
        entity.setsNii(record.getsNii());
        entity.setbNii(record.getbNii());
        entity.setNii(record.getNii());
        entity.setRetail(record.getRetail());
        entity.setEmployee(record.getEmployee());
        entity.setShareholder(record.getShareholder());
        entity.setOthers(record.getOthers());
        entity.setTotal(record.getTotal());
        entity.setApplications(record.getApplications());
        entity.setSubscriptionAsOn(safeParseTimestamp(record.getSubscriptionAsOn()));
        entity.setFetchedAt(LocalDateTime.now());
        return entity;
    }

    private String cleanCompanyName(String html) {
        if (html == null) return null;
        return Jsoup.parse(html).text().replaceAll("\\s+[A-Z]{1,3}$", "").trim();
    }

    private LocalDate safeParseDate(String value) {
        try {
            return LocalDate.parse(value);
        } catch (Exception e) {
            return null;
        }
    }

    private LocalDateTime safeParseTimestamp(String value) {
        try {
            return LocalDateTime.parse(value, SUBSCRIPTION_TS_FORMAT);
        } catch (Exception e) {
            return null;
        }
    }

    private String buildUrl() {
        LocalDate today = LocalDate.now();
        int fyStartYear = today.getMonthValue() >= 4 ? today.getYear() : today.getYear() - 1;
        String fy = String.format("%d-%02d", fyStartYear, (fyStartYear + 1) % 100);
        return String.format(BASE_URL, fyStartYear, fy, category);
    }

    private String normalizeCompanyName(String name) {
        if (name == null) return "";
        return name.toLowerCase()
                .replaceAll("\\b(ltd|limited|ipo)\\b\\.?", "")
                .replaceAll("[^a-z0-9]", "")
                .trim();
    }
}