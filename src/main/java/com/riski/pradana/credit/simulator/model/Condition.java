package com.riski.pradana.credit.simulator.model;

import java.util.Optional;

public enum Condition {
  NEW, USED;

  public static Condition fromString(String s) {
    if (s == null || s.isBlank()) {
      return null;
    }
    return Optional.ofNullable(s)
        .map(String::trim)
        .map(String::toUpperCase)
        .filter(v -> v.equals("NEW") || v.equals("USED"))
        .map(Condition::valueOf)
        .orElse(null);
  }
}
