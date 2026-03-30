package com.riski.pradana.credit.simulator.service.strategy;

import com.riski.pradana.credit.simulator.command.model.CalculateInstallmentCommandResponse;
import com.riski.pradana.credit.simulator.service.InstallmentInterestPolicy;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculateInstallmentServiceImplTest {

  private static final double RATE_DELTA = 0.001;

  @Test
  void standardPolicyMatchesExpectedProgressionForCarBaseRate() {
    CalculateInstallmentServiceImpl service = new CalculateInstallmentServiceImpl(InstallmentInterestPolicy.STANDARD);
    List<CalculateInstallmentCommandResponse.Installment> list =
        service.calculateMonthlyInstallments(8, 100_000_000, 5);
    assertEquals(5, list.size());
    assertEquals(8.0, list.get(0).interestRate(), RATE_DELTA);
    assertEquals(8.1, list.get(1).interestRate(), RATE_DELTA);
    assertEquals(8.6, list.get(2).interestRate(), RATE_DELTA);
    assertEquals(8.7, list.get(3).interestRate(), RATE_DELTA);
    assertEquals(9.2, list.get(4).interestRate(), RATE_DELTA);
  }

  @Test
  void flatPolicyKeepsRateAtBaseAfterYearOne() {
    InstallmentInterestPolicy flat = new InstallmentInterestPolicy(0, 0);
    CalculateInstallmentServiceImpl service = new CalculateInstallmentServiceImpl(flat);
    List<CalculateInstallmentCommandResponse.Installment> list =
        service.calculateMonthlyInstallments(9, 24_000_000, 3);
    assertEquals(3, list.size());
    assertEquals(9.0, list.get(0).interestRate(), RATE_DELTA);
    assertEquals(9.0, list.get(1).interestRate(), RATE_DELTA);
    assertEquals(9.0, list.get(2).interestRate(), RATE_DELTA);
  }

  @Test
  void nullPolicyFallsBackToStandard() {
    CalculateInstallmentServiceImpl service = new CalculateInstallmentServiceImpl(null);
    List<CalculateInstallmentCommandResponse.Installment> list =
        service.calculateMonthlyInstallments(8, 100_000_000, 2);
    assertEquals(8.0, list.get(0).interestRate(), RATE_DELTA);
    assertEquals(8.1, list.get(1).interestRate(), RATE_DELTA);
  }
}
