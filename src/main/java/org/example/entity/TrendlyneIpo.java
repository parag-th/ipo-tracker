package org.example.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "trendlyne_ipo")
public class TrendlyneIpo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ipoId;
    private String companyName;
    private String slug;
    private String category; // which body key it came from, e.g. "upcoming_open"

    private LocalDate bidStartDate;
    private LocalDate bidEndDate;

    private Integer strengthCount;
    private Integer riskCount;
    private Double priceRangeMin;
    private Double priceRangeMax;

    private Double subscriptionValue;
    private String subscriptionText;
    private Double qib;
    private Double hni;
    private Double retail;

    private Integer lotSize;
    private Boolean isOpenNow;
    private Boolean isSme;
    private String exchangeFlags;
    private Double issueSize;
    private String isin;

    private LocalDateTime fetchedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIpoId() { return ipoId; }
    public void setIpoId(Long ipoId) { this.ipoId = ipoId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public LocalDate getBidStartDate() { return bidStartDate; }
    public void setBidStartDate(LocalDate bidStartDate) { this.bidStartDate = bidStartDate; }

    public LocalDate getBidEndDate() { return bidEndDate; }
    public void setBidEndDate(LocalDate bidEndDate) { this.bidEndDate = bidEndDate; }

    public Integer getStrengthCount() { return strengthCount; }
    public void setStrengthCount(Integer strengthCount) { this.strengthCount = strengthCount; }

    public Integer getRiskCount() { return riskCount; }
    public void setRiskCount(Integer riskCount) { this.riskCount = riskCount; }

    public Double getPriceRangeMin() { return priceRangeMin; }
    public void setPriceRangeMin(Double priceRangeMin) { this.priceRangeMin = priceRangeMin; }

    public Double getPriceRangeMax() { return priceRangeMax; }
    public void setPriceRangeMax(Double priceRangeMax) { this.priceRangeMax = priceRangeMax; }

    public Double getSubscriptionValue() { return subscriptionValue; }
    public void setSubscriptionValue(Double subscriptionValue) { this.subscriptionValue = subscriptionValue; }

    public String getSubscriptionText() { return subscriptionText; }
    public void setSubscriptionText(String subscriptionText) { this.subscriptionText = subscriptionText; }

    public Double getQib() { return qib; }
    public void setQib(Double qib) { this.qib = qib; }

    public Double getHni() { return hni; }
    public void setHni(Double hni) { this.hni = hni; }

    public Double getRetail() { return retail; }
    public void setRetail(Double retail) { this.retail = retail; }

    public Integer getLotSize() { return lotSize; }
    public void setLotSize(Integer lotSize) { this.lotSize = lotSize; }

    public Boolean getIsOpenNow() { return isOpenNow; }
    public void setIsOpenNow(Boolean isOpenNow) { this.isOpenNow = isOpenNow; }

    public Boolean getIsSme() { return isSme; }
    public void setIsSme(Boolean isSme) { this.isSme = isSme; }

    public String getExchangeFlags() { return exchangeFlags; }
    public void setExchangeFlags(String exchangeFlags) { this.exchangeFlags = exchangeFlags; }

    public Double getIssueSize() { return issueSize; }
    public void setIssueSize(Double issueSize) { this.issueSize = issueSize; }

    public String getIsin() { return isin; }
    public void setIsin(String isin) { this.isin = isin; }

    public LocalDateTime getFetchedAt() { return fetchedAt; }
    public void setFetchedAt(LocalDateTime fetchedAt) { this.fetchedAt = fetchedAt; }
}