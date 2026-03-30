package com.riski.pradana.credit.simulator.service.strategy;

import com.riski.pradana.credit.simulator.command.model.CalculateInstallmentCommandResponse;
import com.riski.pradana.credit.simulator.service.CalculateInstallmentService;
import com.riski.pradana.credit.simulator.service.InstallmentInterestPolicy;

import java.util.ArrayList;
import java.util.List;

/**
 * Strategy: stepped annual interest after year 1 (odd/even year increments via {@link InstallmentInterestPolicy}).
 */
public final class CalculateInstallmentServiceImpl implements CalculateInstallmentService {

  private final InstallmentInterestPolicy interestPolicy;

  public CalculateInstallmentServiceImpl() {
    this(InstallmentInterestPolicy.defaultPolicy());
  }

  public CalculateInstallmentServiceImpl(InstallmentInterestPolicy interestPolicy) {
    this.interestPolicy = interestPolicy != null ? interestPolicy : InstallmentInterestPolicy.defaultPolicy();
  }

  private double nextAnnualRate(int year, double baseRate, double currentRate) {
    if (year == 1) {
      return baseRate;
    }
    int yearsPassed = year - 1;
    if (yearsPassed % 2 == 0) {
      return currentRate + interestPolicy.rateIncrementWhenYearsPassedIsEven();
    }
    return currentRate + interestPolicy.rateIncrementWhenYearsPassedIsOdd();
  }

  @Override
  public List<CalculateInstallmentCommandResponse.Installment> calculateMonthlyInstallments(double baseRate,
      double totalLoan,
      int tenure) {

    List<CalculateInstallmentCommandResponse.Installment> installments = new ArrayList<>();

    double baseInstallment = totalLoan / (tenure * 12);
    double currentRate = baseRate;

    for (int year = 1; year <= tenure; year++) {
      currentRate = nextAnnualRate(year, baseRate, currentRate);
      double monthly = InstallmentCalculationUtil.monthly(currentRate, totalLoan, baseInstallment);
      CalculateInstallmentCommandResponse.Installment installment =
          new CalculateInstallmentCommandResponse.Installment(year, monthly, currentRate);
      installments.add(installment);
    }
    return installments;
  }
}
