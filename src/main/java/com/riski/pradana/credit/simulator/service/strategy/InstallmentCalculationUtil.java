package com.riski.pradana.credit.simulator.service.strategy;

final class InstallmentCalculationUtil {

  private InstallmentCalculationUtil() {
  }

  static double monthly(double interestRate, double totalLoan, double baseInstallment) {
    double totalMonthlyInterest = (totalLoan * interestRate / 100) / 12;
    return baseInstallment + totalMonthlyInterest;
  }
}
