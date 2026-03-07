package com.riski.pradana.credit.simulator.client.model;

public record Response<T>(boolean success, T data, String error) {

  public static <T> Response<T> success(T data) {
    return new Response<>(true, data, null);
  }

  public static <T> Response<T> failure(String error) {
    return new Response<>(false, null, error);
  }
}
