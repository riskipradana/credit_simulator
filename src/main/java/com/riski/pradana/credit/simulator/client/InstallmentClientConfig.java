package com.riski.pradana.credit.simulator.client;

import java.time.Duration;

public class InstallmentClientConfig {

  private static final String DEFAULT_URL = "http://www.mocky.io/v2/5d06e6ae3000005300051d16";
  private static final Duration DEFAULT_CONNECT_TIMEOUT = Duration.ofSeconds(10);
  private static final Duration DEFAULT_REQUEST_TIMEOUT = Duration.ofSeconds(10);

  private final String baseUrl;
  private final Duration connectTimeout;
  private final Duration requestTimeout;

  public InstallmentClientConfig(String baseUrl, Duration connectTimeout, Duration requestTimeout) {
    this.baseUrl = baseUrl != null && !baseUrl.isBlank() ? baseUrl.trim() : DEFAULT_URL;
    this.connectTimeout = connectTimeout != null ? connectTimeout : DEFAULT_CONNECT_TIMEOUT;
    this.requestTimeout = requestTimeout != null ? requestTimeout : DEFAULT_REQUEST_TIMEOUT;
  }

  public String getBaseUrl() {
    return baseUrl;
  }

  public Duration getConnectTimeout() {
    return connectTimeout;
  }

  public Duration getRequestTimeout() {
    return requestTimeout;
  }

  public static InstallmentClientConfig defaultConfig() {
    String url = System.getenv("INSTALLMENT_API_URL");
    return new InstallmentClientConfig(url, DEFAULT_CONNECT_TIMEOUT, DEFAULT_REQUEST_TIMEOUT);
  }
}
