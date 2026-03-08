package com.riski.pradana.credit.simulator.client.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.riski.pradana.credit.simulator.client.InstallmentClient;
import com.riski.pradana.credit.simulator.client.config.InstallmentClientConfig;
import com.riski.pradana.credit.simulator.client.model.InstallmentClientResponse;
import com.riski.pradana.credit.simulator.client.model.Response;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class InstallmentClientImpl implements InstallmentClient {

  private final InstallmentClientConfig config;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public InstallmentClientImpl(InstallmentClientConfig config) {
    this.config = config != null ? config : InstallmentClientConfig.defaultConfig();
  }

  @Override
  public Response<InstallmentClientResponse> fetchInstallment() {
    try {
      HttpClient client = HttpClient.newBuilder()
          .connectTimeout(config.getConnectTimeout())
          .build();
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(config.getBaseUrl()))
          .timeout(config.getRequestTimeout())
          .GET()
          .build();
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() != 200) {
        return Response.failure(response.statusCode(), "HTTP error: " + response.statusCode());
      }
      String body = response.body();
      try {
        InstallmentClientResponse data = objectMapper.readValue(body, InstallmentClientResponse.class);
        return Response.success(data);
      } catch (Exception e) {
        return Response.failure(500, "Invalid JSON: " + e.getMessage());
      }
    } catch (Exception e) {
      return Response.failure("Connection failed: " + e.getMessage());
    }
  }
}
