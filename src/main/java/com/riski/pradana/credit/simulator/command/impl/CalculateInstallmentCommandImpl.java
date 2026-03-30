package com.riski.pradana.credit.simulator.command.impl;

import com.riski.pradana.credit.simulator.command.CalculateInstallmentCommand;
import com.riski.pradana.credit.simulator.command.model.CalculateInstallmentCommandRequest;
import com.riski.pradana.credit.simulator.command.model.CalculateInstallmentCommandResponse;
import com.riski.pradana.credit.simulator.constant.BaseInterestRate;
import com.riski.pradana.credit.simulator.constant.Condition;
import com.riski.pradana.credit.simulator.constant.VehicleType;
import com.riski.pradana.credit.simulator.service.CalculateInstallmentService;
import com.riski.pradana.credit.simulator.service.strategy.CalculateInstallmentServiceImpl;

import java.time.Year;
import java.util.List;

public class CalculateInstallmentCommandImpl implements CalculateInstallmentCommand {

  private final CalculateInstallmentService loanService;

  public CalculateInstallmentCommandImpl() {
    this(new CalculateInstallmentServiceImpl());
  }

  public CalculateInstallmentCommandImpl(CalculateInstallmentService loanService) {
    this.loanService = loanService;
  }

  @Override
  public CalculateInstallmentCommandResponse execute(CalculateInstallmentCommandRequest request) {

    VehicleType vehicleType = parseVehicleType(request.vehicleType());
    Condition vehicleCondition = parseCondition(request.vehicleCondition());

    validate(request, vehicleType, vehicleCondition);

    int baseRate = vehicleType == VehicleType.MOBIL ? BaseInterestRate.car : BaseInterestRate.motorcycle;

    List<CalculateInstallmentCommandResponse.Installment> installments =
        loanService.calculateMonthlyInstallments(baseRate, request.totalLoan(), request.tenure());

    return new CalculateInstallmentCommandResponse(installments);
  }

  private static VehicleType parseVehicleType(String value) {
    VehicleType parsed = VehicleType.fromString(value);
    if (parsed == null) {
      throw new IllegalArgumentException("Jenis Kendaraan harus Motor atau Mobil");
    }
    return parsed;
  }

  private static Condition parseCondition(String value) {
    Condition parsed = Condition.fromString(value);
    if (parsed == null) {
      throw new IllegalArgumentException("Kondisi Kendaraan harus NEW atau USED");
    }
    return parsed;
  }

  private void validate(CalculateInstallmentCommandRequest request,
      VehicleType vehicleType,
      Condition vehicleCondition) {

    int currentYear = Year.now().getValue();

    if (vehicleCondition == Condition.NEW && request.vehicleYear() < (currentYear - 1)) {
      throw new IllegalArgumentException("Kendaraan Baru tidak boleh lebih tua dari tahun " + (currentYear - 1));
    }

    if (request.tenure() < 1 || request.tenure() > 6) {
      throw new IllegalArgumentException("Tenor harus 1-6 tahun");
    }

    if (request.totalLoan() <= 0 || request.totalLoan() > 1_000_000_000) {
      throw new IllegalArgumentException("Jumlah Pinjaman harus <= 1.000.000.000");
    }

    double dpPercent = (request.totalDownPayment() / request.totalLoan()) * 100;
    if (vehicleCondition == Condition.NEW) {
      if (vehicleType == VehicleType.MOBIL && dpPercent < 35) {
        throw new IllegalArgumentException("DP Mobil Baru minimal 35% dari total pinjaman");
      }
      if (vehicleType == VehicleType.MOTOR && dpPercent < 25) {
        throw new IllegalArgumentException("DP Motor Baru minimal 25% dari total pinjaman");
      }
    }
  }
}
