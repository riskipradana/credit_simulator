package com.riski.pradana.credit.simulator.command.impl;

import com.riski.pradana.credit.simulator.command.CalculateInstallmentCommand;
import com.riski.pradana.credit.simulator.command.model.CalculateInstallmentCommandRequest;
import com.riski.pradana.credit.simulator.command.model.CalculateInstallmentCommandResponse;
import com.riski.pradana.credit.simulator.model.BaseInterestRate;
import com.riski.pradana.credit.simulator.service.CalculateInstallmentService;
import com.riski.pradana.credit.simulator.service.impl.CalculateInstallmentServiceImpl;

import java.time.Year;
import java.util.List;

public class CalculateInstallmentCommandImpl implements CalculateInstallmentCommand {

  private final CalculateInstallmentService loanService = new CalculateInstallmentServiceImpl();

  @Override
  public CalculateInstallmentCommandResponse execute(CalculateInstallmentCommandRequest request) {

    validate(request);

    int baseRate = "Mobil".equalsIgnoreCase(request.vehicleType()) ? BaseInterestRate.car : BaseInterestRate.motorcycle;

    List<CalculateInstallmentCommandResponse.Installment> installments =
        loanService.calculateMonthlyInstallments(baseRate, request.totalLoan(), request.tenure());

    return new CalculateInstallmentCommandResponse(installments);
  }

  private void validate(CalculateInstallmentCommandRequest request) {

    int currentYear = Year.now().getValue();

    if (!(request.vehicleType().equalsIgnoreCase("Motor") || request.vehicleType().equalsIgnoreCase("Mobil"))) {
      throw new IllegalArgumentException("Jenis Kendaraan harus Motor atau Mobil");
    }

    if (!(request.vehicleCondition().equalsIgnoreCase("Bekas") || request.vehicleCondition()
        .equalsIgnoreCase("Baru"))) {
      throw new IllegalArgumentException("Kondisi Kendaraan harus Bekas atau Baru");
    }

    if (request.vehicleCondition().equalsIgnoreCase("Baru") && request.vehicleYear() < (currentYear - 1)) {
      throw new IllegalArgumentException("Kendaraan Baru tidak boleh lebih tua dari tahun " + (currentYear - 1));
    }

    if (request.tenure() < 1 || request.tenure() > 6) {
      throw new IllegalArgumentException("Tenor harus 1-6 tahun");
    }

    if (request.totalLoan() <= 0 || request.totalLoan() > 1_000_000_000) {
      throw new IllegalArgumentException("Jumlah Pinjaman harus <= 1.000.000.000");
    }

    double dpPercent = (request.totalDownPayment() / request.totalLoan()) * 100;
    if (request.vehicleCondition().equalsIgnoreCase("Baru")) {
      if (request.vehicleType().equalsIgnoreCase("Mobil") && dpPercent < 35) {
        throw new IllegalArgumentException("DP Mobil Baru minimal 35% dari total pinjaman");
      }
      if (request.vehicleType().equalsIgnoreCase("Motor") && dpPercent < 25) {
        throw new IllegalArgumentException("DP Motor Baru minimal 25% dari total pinjaman");
      }
    }
  }
}
