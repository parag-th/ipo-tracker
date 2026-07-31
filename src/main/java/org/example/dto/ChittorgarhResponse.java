package org.example.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChittorgarhResponse {

    @JsonProperty("reportTableData")
    private List<IpoRecord> reportTableData;

    @JsonProperty("totalRecords")
    private int totalRecords;

    public List<IpoRecord> getReportTableData() { return reportTableData; }
    public void setReportTableData(List<IpoRecord> reportTableData) { this.reportTableData = reportTableData; }

    public int getTotalRecords() { return totalRecords; }
    public void setTotalRecords(int totalRecords) { this.totalRecords = totalRecords; }
}