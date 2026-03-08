package com.riski.pradana.credit.simulator.client;

import com.riski.pradana.credit.simulator.client.model.Response;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class InstallmentClient {

  private static final String URL = "http://www.mocky.io/v2/5d06e6ae3000005300051d16";

  public Response<String> fetchInstallment() {
    try {
      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(URL))
          .GET()
          .build();
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() != 200) {
        return Response.failure(response.statusCode(), "HTTP error: " + response.statusCode());
      }
      return Response.success(response.body());
    } catch (Exception e) {
      return Response.failure("Connection failed: " + e.getMessage());}
  }
}
