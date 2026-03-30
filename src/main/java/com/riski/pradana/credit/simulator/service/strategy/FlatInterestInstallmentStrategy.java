package com.riski.pradana.credit.simulator.service.strategy;

import com.riski.pradana.credit.simulator.command.model.CalculateInstallmentCommandResponse;
import com.riski.pradana.credit.simulator.service.CalculateInstallmentService;

import java.util.ArrayList;
import java.util.List;

/**
 * Strategy: constant annual interest rate ({@code baseRate}) for every year of the schedule.
 */
public final class FlatInterestInstallmentStrategy implements CalculateInstallmentService {

  @Override
  public List<CalculateInstallmentCommandResponse.Installment> calculateMonthlyInstallments(double baseRate,
      double totalLoan,
      int tenure) {

    List<CalculateInstallmentCommandResponse.Installment> installments = new ArrayList<>();
    double baseInstallment = totalLoan / (tenure * 12);

    for (int year = 1; year <= tenure; year++) {
      double monthly = InstallmentScheduleMath.monthlyInstallment(baseRate, totalLoan, baseInstallment);
      installments.add(new CalculateInstallmentCommandResponse.Installment(year, monthly, baseRate));
    }
    return installments;
  }
}
