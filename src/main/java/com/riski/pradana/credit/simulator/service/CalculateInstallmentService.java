package com.riski.pradana.credit.simulator.service;

public interface CalculateInstallmentService {
  double calculateInterestRate(int year, double baseRate, double currentRate);
  double calculateMonthlyInstallment(double interestRate, double remainingLoan, double baseInstallment);
}
