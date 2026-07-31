package org.example.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IpoRecord {

    @JsonProperty("~id")
    private Long id;

    @JsonProperty("Company")
    private String companyHtml;

    @JsonProperty("~URLRewrite_Folder_Name")
    private String slug;

    @JsonProperty("~Issue_Open_Date")
    private String issueOpenDate;

    @JsonProperty("~Issue_Close_Date")
    private String issueCloseDate;

    @JsonProperty("QIB (x)")
    @JsonDeserialize(using = FlexibleDoubleDeserializer.class)
    private Double qib;

    @JsonProperty("sNII (x)")
    @JsonDeserialize(using = FlexibleDoubleDeserializer.class)
    private Double sNii;

    @JsonProperty("bNII (x)")
    @JsonDeserialize(using = FlexibleDoubleDeserializer.class)
    private Double bNii;

    @JsonProperty("NII (x)")
    @JsonDeserialize(using = FlexibleDoubleDeserializer.class)
    private Double nii;

    @JsonProperty("Retail (x)")
    @JsonDeserialize(using = FlexibleDoubleDeserializer.class)
    private Double retail;

    @JsonProperty("Employee (x)")
    @JsonDeserialize(using = FlexibleDoubleDeserializer.class)
    private Double employee;

    @JsonProperty("Shareholder (x)")
    @JsonDeserialize(using = FlexibleDoubleDeserializer.class)
    private Double shareholder;

    @JsonProperty("Others (x)")
    @JsonDeserialize(using = FlexibleDoubleDeserializer.class)
    private Double others;

    @JsonProperty("Total (x)")
    @JsonDeserialize(using = FlexibleDoubleDeserializer.class)
    private Double total;

    @JsonProperty("Applications")
    private String applications;

    @JsonProperty("Subscription as on")
    private String subscriptionAsOn;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCompanyHtml() { return companyHtml; }
    public void setCompanyHtml(String companyHtml) { this.companyHtml = companyHtml; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getIssueOpenDate() { return issueOpenDate; }
    public void setIssueOpenDate(String issueOpenDate) { this.issueOpenDate = issueOpenDate; }

    public String getIssueCloseDate() { return issueCloseDate; }
    public void setIssueCloseDate(String issueCloseDate) { this.issueCloseDate = issueCloseDate; }

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

    public String getSubscriptionAsOn() { return subscriptionAsOn; }
    public void setSubscriptionAsOn(String subscriptionAsOn) { this.subscriptionAsOn = subscriptionAsOn; }
}