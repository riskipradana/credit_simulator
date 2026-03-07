package com.riski.pradana.credit.simulator.command.model;

import java.util.List;

public class CalculateInstallmentCommandResponse {

  private List<Installment> installments;

  public CalculateInstallmentCommandResponse(List<Installment> installments) {
    this.installments = installments;
  }

  public List<Installment> getInstallments() {
    return installments;
  }

  public void setInstallments(List<Installment> installments) {
    this.installments = installments;
  }

  public static class Installment {
    private int year;
    private double monthlyInstallment;
    private double interestRate;

    public int getYear() {
      return year;
    }

    public void setYear(int year) {
      this.year = year;
    }

    public double getMonthlyInstallment() {
      return monthlyInstallment;
    }

    public void setMonthlyInstallment(double monthlyInstallment) {
      this.monthlyInstallment = monthlyInstallment;
    }

    public double getInterestRate() {
      return interestRate;
    }

    public void setInterestRate(double interestRate) {
      this.interestRate = interestRate;
    }
  }
}
