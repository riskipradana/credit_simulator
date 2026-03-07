package com.riski.pradana.credit.simulator.command.model;

import java.util.List;

public class GetInstallmentCommandResponse {
  private List<Installment> installments;

  public static class Installment {
    private String year;
    private double monthlyInstallment;
    private double interestRate;
  }
}
