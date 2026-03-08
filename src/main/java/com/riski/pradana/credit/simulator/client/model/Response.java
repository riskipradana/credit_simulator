package com.riski.pradana.credit.simulator.client.model;

public record Response<T>(int code, T data, String error) {

  public boolean success() {
    return code >= 200 && code < 300;
  }

  public static <T> Response<T> success(T data) {
    return new Response<>(200, data, null);
  }

  public static <T> Response<T> failure(String error) {
    return new Response<>(500, null, error);
  }

  public static <T> Response<T> failure(int code, String error) {
    return new Response<>(code, null, error);
  }
}
