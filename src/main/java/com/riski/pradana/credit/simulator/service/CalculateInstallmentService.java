package com.riski.pradana.credit.simulator.service;

import com.riski.pradana.credit.simulator.command.model.CalculateInstallmentCommandResponse;

import java.util.List;

public interface CalculateInstallmentService {
  List<CalculateInstallmentCommandResponse.Installment> calculateMonthlyInstallments(double baseRate,
      double totalLoan, int tenure);
}
