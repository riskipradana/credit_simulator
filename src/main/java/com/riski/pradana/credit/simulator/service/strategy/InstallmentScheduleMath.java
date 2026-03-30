package com.riski.pradana.credit.simulator.service.strategy;

final class InstallmentScheduleMath {

  private InstallmentScheduleMath() {
  }

  static double monthlyInstallment(double interestRate, double totalLoan, double baseInstallment) {
    double totalMonthlyInterest = (totalLoan * interestRate / 100) / 12;
    return baseInstallment + totalMonthlyInterest;
  }
}
