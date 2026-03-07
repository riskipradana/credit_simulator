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

    int baseRate = "Mobil".equalsIgnoreCase(request.vehicleType()) ? BaseInterestRate.car : BaseInterestRate.motorcycle;

    List<CalculateInstallmentCommandResponse.Installment> installments = new ArrayList<>();

    double baseInstallment = request.totalLoan() / (request.tenure() * 12);

    for (int year = 1; year <= request.tenure(); year++) {

      double interestRate = loanService.calculateInterestRate(year, baseRate);
      double remainingLoan = request.totalLoan() - (baseInstallment * (year * 12));
      double monthlyInstallment = loanService.calculateMonthlyInstallment(interestRate, remainingLoan);

      CalculateInstallmentCommandResponse.Installment installment =
          new CalculateInstallmentCommandResponse.Installment();
      installment.setYear(year);
      installment.setMonthlyInstallment(monthlyInstallment);
      installment.setInterestRate(interestRate);

      installments.add(installment);
    }
    return new CalculateInstallmentCommandResponse(installments);
  }
}
