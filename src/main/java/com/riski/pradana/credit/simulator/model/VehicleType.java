package com.riski.pradana.credit.simulator.model;

import java.util.Optional;

public enum VehicleType {
  MOBIL,
  MOTOR;

  public static VehicleType fromString(String s) {
    if (s == null || s.isBlank()) {
      return null;
    }
    return Optional.ofNullable(s)
        .map(String::trim)
        .map(String::toUpperCase)
        .filter(v -> v.equals("MOBIL") || v.equals("MOTOR"))
        .map(VehicleType::valueOf)
        .orElse(null);
  }
}
