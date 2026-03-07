package com.riski.pradana.credit.simulator.service.impl;

import com.riski.pradana.credit.simulator.command.model.CalculateInstallmentCommandResponse;
import com.riski.pradana.credit.simulator.service.CalculateInstallmentService;

import java.util.ArrayList;
import java.util.List;

public class CalculateInstallmentServiceImpl implements CalculateInstallmentService {

  public static double interestYoYEveryYear = 0.1;
  public static double interestYoYEveryTwoYear = 0.5;

  private double calculateInterestRate(int year, double baseRate, double currentRate) {
    if (year == 1) {
      return baseRate;
    }
    int yearsPassed = year - 1;
    if (yearsPassed % 2 == 0) {
      return currentRate + interestYoYEveryTwoYear;
    }
    return currentRate + interestYoYEveryYear;
  }

  private double calculateMonthlyInstallment(double interestRate, double totalLoan, double baseInstallment) {
    double totalMonthlyInterest = (totalLoan * interestRate / 100) / 12;
    return baseInstallment + totalMonthlyInterest;
  }

  @Override
  public List<CalculateInstallmentCommandResponse.Installment> calculateMonthlyInstallments(double baseRate,
      double totalLoan,
      int tenure) {

    List<CalculateInstallmentCommandResponse.Installment> installments = new ArrayList<>();

    double baseInstallment = totalLoan / (tenure * 12);
    double currentRate = baseRate;

    for (int year = 1; year <= tenure; year++) {
      currentRate = this.calculateInterestRate(year, baseRate, currentRate);
      double monthlyInstallment = this.calculateMonthlyInstallment(currentRate, totalLoan, baseInstallment);
      CalculateInstallmentCommandResponse.Installment installment =
          new CalculateInstallmentCommandResponse.Installment(year, monthlyInstallment, currentRate);
      installments.add(installment);
    }
    return installments;
  }
}
