package com.riski.pradana.credit.simulator.command.impl;

import com.riski.pradana.credit.simulator.command.CalculateInstallmentCommand;
import com.riski.pradana.credit.simulator.command.model.CalculateInstallmentCommandRequest;
import com.riski.pradana.credit.simulator.command.model.CalculateInstallmentCommandResponse;
import com.riski.pradana.credit.simulator.model.BaseInterestRate;
import com.riski.pradana.credit.simulator.service.CalculateInstallmentService;
import com.riski.pradana.credit.simulator.service.impl.CalculateInstallmentServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class CalculateInstallmentCommandImpl implements CalculateInstallmentCommand {

  private final CalculateInstallmentService loanService = new CalculateInstallmentServiceImpl();

  @Override
  public CalculateInstallmentCommandResponse execute(CalculateInstallmentCommandRequest request) {

    int baseRate = "Mobil".equalsIgnoreCase(request.vehicleType())
        ? BaseInterestRate.car :
        BaseInterestRate.motorcycle;

    List<CalculateInstallmentCommandResponse.Installment> installments = new ArrayList<>();

    double baseInstallment = request.totalLoan() / (request.tenure() * 12);
    double currentRate = baseRate;

    for (int year = 1; year <= request.tenure(); year++) {
      currentRate = loanService.calculateInterestRate(year, baseRate, currentRate);
      double monthlyInstallment = loanService.calculateMonthlyInstallment(currentRate, request.totalLoan(),
          baseInstallment);
      CalculateInstallmentCommandResponse.Installment installment =
          new CalculateInstallmentCommandResponse.Installment(year, monthlyInstallment, currentRate);
      installments.add(installment);
    }
    return new CalculateInstallmentCommandResponse(installments);
  }
}
