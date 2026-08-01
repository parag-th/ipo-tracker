package org.example.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TrendlyneIpoRecord {

    @JsonProperty("company_name")
    private String companyName;

    @JsonProperty("ipo_id")
    private Long ipoId;

    @JsonProperty("company_slug_name")
    private String slug;

    @JsonProperty("bid_start_date")
    private String bidStartDate; // "30 Jul, 2026"

    @JsonProperty("bid_end_date")
    private String bidEndDate;

    @JsonProperty("strength_count")
    private Integer strengthCount;

    @JsonProperty("risk_count")
    private Integer riskCount;

    @JsonProperty("price_range_min")
    private Double priceRangeMin;

    @JsonProperty("price_range_max")
    private Double priceRangeMax;

    @JsonProperty("subscription_value")
    private Double subscriptionValue;

    @JsonProperty("subscription_text")
    private String subscriptionText;

    @JsonProperty("qib")
    private Double qib;

    @JsonProperty("hni")
    private Double hni;

    @JsonProperty("retail")
    private Double retail;

    @JsonProperty("lot_size")
    private Integer lotSize;

    @JsonProperty("is_open_now")
    private Boolean isOpenNow;

    @JsonProperty("is_sme")
    private Boolean isSme;

    @JsonProperty("exchange_flags")
    private String exchangeFlags;

    @JsonProperty("issue_size")
    private Double issueSize;

    @JsonProperty("isin")
    private String isin;

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public Long getIpoId() { return ipoId; }
    public void setIpoId(Long ipoId) { this.ipoId = ipoId; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getBidStartDate() { return bidStartDate; }
    public void setBidStartDate(String bidStartDate) { this.bidStartDate = bidStartDate; }

    public String getBidEndDate() { return bidEndDate; }
    public void setBidEndDate(String bidEndDate) { this.bidEndDate = bidEndDate; }

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
}