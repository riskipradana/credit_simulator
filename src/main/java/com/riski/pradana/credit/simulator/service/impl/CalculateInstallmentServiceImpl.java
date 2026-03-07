package com.riski.pradana.credit.simulator.service.impl;

import com.riski.pradana.credit.simulator.service.CalculateInstallmentService;

public class CalculateInstallmentServiceImpl implements CalculateInstallmentService {

  public static double interestYoYEveryYear = 0.1;
  public static double interestYoYEveryTwoYear = 0.5;

  @Override
  public double calculateInterestRate(int year, double baseRate, double currentRate) {
    if (year == 1) {
      return baseRate;
    }
    int yearsPassed = year - 1;
    if (yearsPassed % 2 == 0) {
      return currentRate + interestYoYEveryTwoYear;
    }
    return currentRate + interestYoYEveryYear;
  }

  @Override
  public double calculateMonthlyInstallment(double interestRate, double totalLoan, double baseInstallment) {
    double totalMonthlyInterest = (totalLoan * interestRate / 100) / 12;
    return baseInstallment + totalMonthlyInterest;
  }
}
