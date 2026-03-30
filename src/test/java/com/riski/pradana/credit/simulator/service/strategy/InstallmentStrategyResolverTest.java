package com.riski.pradana.credit.simulator.service.strategy;

import com.riski.pradana.credit.simulator.service.CalculateInstallmentService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class InstallmentStrategyResolverTest {

  @Test
  void defaultStrategyIsStepped() {
    CalculateInstallmentService s = InstallmentStrategyResolver.defaultStrategy();
    assertInstanceOf(SteppedInterestInstallmentStrategy.class, s);
  }

  @Test
  void resolveForNameFlatIsCaseInsensitive() {
    assertInstanceOf(FlatInterestInstallmentStrategy.class, InstallmentStrategyResolver.resolveForName("FLAT"));
    assertInstanceOf(FlatInterestInstallmentStrategy.class, InstallmentStrategyResolver.resolveForName("flat"));
    assertInstanceOf(FlatInterestInstallmentStrategy.class, InstallmentStrategyResolver.resolveForName("  FLAT  "));
  }

  @Test
  void resolveForNameUnknownUsesStepped() {
    assertInstanceOf(SteppedInterestInstallmentStrategy.class, InstallmentStrategyResolver.resolveForName(""));
    assertInstanceOf(SteppedInterestInstallmentStrategy.class, InstallmentStrategyResolver.resolveForName("STEPPED"));
    assertInstanceOf(SteppedInterestInstallmentStrategy.class, InstallmentStrategyResolver.resolveForName(null));
  }
}
