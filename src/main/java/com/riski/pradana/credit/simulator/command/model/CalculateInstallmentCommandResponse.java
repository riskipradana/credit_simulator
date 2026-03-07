package com.riski.pradana.credit.simulator.command.model;

import java.util.List;

public record CalculateInstallmentCommandResponse(List<Installment> installments) {

  public record Installment(int year, double monthlyInstallment, double interestRate) {}
}
