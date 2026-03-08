package com.riski.pradana.credit.simulator.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record InstallmentClientResponse(
    @JsonProperty("monthlyInstallment") String monthlyInstallment,
    @JsonProperty("data") String data
) {

  public String getDisplayValue() {
    if (monthlyInstallment != null && !monthlyInstallment.isEmpty()) return monthlyInstallment;
    if (data != null && !data.isEmpty()) return data;
    return "";
  }
}
