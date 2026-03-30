package com.riski.pradana.credit.simulator.service.strategy;

import com.riski.pradana.credit.simulator.service.CalculateInstallmentService;

/**
 * Selects the installment {@link CalculateInstallmentService} strategy from configuration.
 */
public final class InstallmentStrategyResolver {

  public static final String ENV_INSTALLMENT_STRATEGY = "INSTALLMENT_STRATEGY";
  private static final String FLAT = "FLAT";

  private InstallmentStrategyResolver() {
  }

  public static CalculateInstallmentService defaultStrategy() {
    return new SteppedInterestInstallmentStrategy();
  }

  /**
   * Resolves strategy from {@value #ENV_INSTALLMENT_STRATEGY}. {@code FLAT} selects
   * {@link FlatInterestInstallmentStrategy}; any other value (including unset) uses stepped default.
   */
  public static CalculateInstallmentService resolveFromEnvironment() {
    return resolveForName(System.getenv(ENV_INSTALLMENT_STRATEGY));
  }

  /**
   * For tests and tooling: {@code FLAT} (case-insensitive) returns flat strategy; otherwise stepped.
   */
  public static CalculateInstallmentService resolveForName(String name) {
    if (name != null && FLAT.equalsIgnoreCase(name.trim())) {
      return new FlatInterestInstallmentStrategy();
    }
    return new SteppedInterestInstallmentStrategy();
  }
}
