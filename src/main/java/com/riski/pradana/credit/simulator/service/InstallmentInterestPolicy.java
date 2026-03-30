package com.riski.pradana.credit.simulator.service;

/**
 * Immutable policy for how the annual interest rate steps after year 1 (strategy configuration).
 * Year 1 always uses {@code baseRate}; later years add increments based on {@code year - 1}.
 */
public record InstallmentInterestPolicy(
    /** Added when {@code (year - 1) % 2 == 1} (e.g. year 2, 4, 6). */
    double rateIncrementWhenYearsPassedIsOdd,
    /** Added when {@code (year - 1) % 2 == 0} and {@code year > 1} (e.g. year 3, 5). */
    double rateIncrementWhenYearsPassedIsEven
) {

  public static final InstallmentInterestPolicy STANDARD =
      new InstallmentInterestPolicy(0.1, 0.5);

  public static InstallmentInterestPolicy defaultPolicy() {
    return STANDARD;
  }
}
