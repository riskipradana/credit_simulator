package com.riski.pradana.credit.simulator.service;

public interface CalculateInstallmentService {
  double calculateInterestRate(int year, int baseRate);
  double calculateMonthlyInstallment(double interestRate, double remainingLoan);
}
