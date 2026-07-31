package org.example.service;

import org.example.dto.ChittorgarhResponse;
import org.example.dto.IpoRecord;
import org.example.entity.IpoSubscription;
import org.example.repository.IpoSubscriptionRepository;
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

    @Value("${ipo.tracker.category:mainboard}")
    private String category;

    public IpoDataService(RestTemplateBuilder builder, IpoSubscriptionRepository repository) {
        this.restTemplate = builder
                .defaultHeader("User-Agent", "Mozilla/5.0 (compatible; PersonalIpoTracker/1.0)")
                .build();
        this.repository = repository;
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
            repository.save(mapToEntity(record));
            savedCount++;
        }
        log.info("Saved {} open IPO subscription snapshots", savedCount);
    }

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
}