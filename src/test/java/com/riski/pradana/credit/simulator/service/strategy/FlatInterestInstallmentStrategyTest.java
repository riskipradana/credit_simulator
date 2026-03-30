package com.riski.pradana.credit.simulator.service.strategy;

import com.riski.pradana.credit.simulator.command.model.CalculateInstallmentCommandResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FlatInterestInstallmentStrategyTest {

  private static final double RATE_DELTA = 0.001;

  @Test
  void keepsBaseRateForAllYears() {
    FlatInterestInstallmentStrategy strategy = new FlatInterestInstallmentStrategy();
    List<CalculateInstallmentCommandResponse.Installment> list =
        strategy.calculateMonthlyInstallments(8, 100_000_000, 3);
    assertEquals(3, list.size());
    assertEquals(8.0, list.get(0).interestRate(), RATE_DELTA);
    assertEquals(8.0, list.get(1).interestRate(), RATE_DELTA);
    assertEquals(8.0, list.get(2).interestRate(), RATE_DELTA);
  }
}
