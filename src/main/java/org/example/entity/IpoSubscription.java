package org.example.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ipo_subscription")
public class IpoSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chittorgarhId;
    private String companyName;
    private String slug;
    private LocalDate openingDate;
    private LocalDate closingDate;

    private Double qib;
    private Double sNii;
    private Double bNii;
    private Double nii;
    private Double retail;
    private Double employee;
    private Double shareholder;
    private Double others;
    private Double total;

    private String applications;
    private LocalDateTime subscriptionAsOn;
    private LocalDateTime fetchedAt;
    private String isin;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getChittorgarhId() { return chittorgarhId; }
    public void setChittorgarhId(Long chittorgarhId) { this.chittorgarhId = chittorgarhId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public LocalDate getOpeningDate() { return openingDate; }
    public void setOpeningDate(LocalDate openingDate) { this.openingDate = openingDate; }

    public LocalDate getClosingDate() { return closingDate; }
    public void setClosingDate(LocalDate closingDate) { this.closingDate = closingDate; }

    public Double getQib() { return qib; }
    public void setQib(Double qib) { this.qib = qib; }

    public Double getsNii() { return sNii; }
    public void setsNii(Double sNii) { this.sNii = sNii; }

    public Double getbNii() { return bNii; }
    public void setbNii(Double bNii) { this.bNii = bNii; }

    public Double getNii() { return nii; }
    public void setNii(Double nii) { this.nii = nii; }

    public Double getRetail() { return retail; }
    public void setRetail(Double retail) { this.retail = retail; }

    public Double getEmployee() { return employee; }
    public void setEmployee(Double employee) { this.employee = employee; }

    public Double getShareholder() { return shareholder; }
    public void setShareholder(Double shareholder) { this.shareholder = shareholder; }

    public Double getOthers() { return others; }
    public void setOthers(Double others) { this.others = others; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public String getApplications() { return applications; }
    public void setApplications(String applications) { this.applications = applications; }

    public LocalDateTime getSubscriptionAsOn() { return subscriptionAsOn; }
    public void setSubscriptionAsOn(LocalDateTime subscriptionAsOn) { this.subscriptionAsOn = subscriptionAsOn; }

    public LocalDateTime getFetchedAt() { return fetchedAt; }
    public void setFetchedAt(LocalDateTime fetchedAt) { this.fetchedAt = fetchedAt; }

    public String getIsin() { return isin; }
    public void setIsin(String isin) { this.isin = isin; }
}