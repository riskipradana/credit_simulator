package com.riski.pradana.credit.simulator.service.impl;

import com.riski.pradana.credit.simulator.service.CalculateInstallmentService;

public class CalculateInstallmentServiceImpl implements CalculateInstallmentService {

  public static double interestYoYEveryYear = 0.1;
  public static double interestYoYEveryTwoYear = 0.5;

  @Override
  public double calculateInterestRate(int year, int baseRate) {
    int yearsPassed = year - 1;

    double yearlyIncrease = yearsPassed * interestYoYEveryYear;
    double twoYearIncrease = ((double) yearsPassed / 2) * interestYoYEveryTwoYear;

    return baseRate + yearlyIncrease + twoYearIncrease;
  }

  @Override
  public double calculateMonthlyInstallment(double interestRate, double remainingLoan) {
    double totalInterest = remainingLoan * interestRate;
    double totalLoan = totalInterest + remainingLoan;
    return totalLoan / 12;
  }
}
