package com.riski.pradana.credit.simulator;

import com.riski.pradana.credit.simulator.command.impl.CalculateInstallmentCommandImpl;
import com.riski.pradana.credit.simulator.command.model.CalculateInstallmentCommandRequest;
import com.riski.pradana.credit.simulator.command.model.CalculateInstallmentCommandResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CalculateInstallmentTest {

  private final CalculateInstallmentCommandImpl command =
      new CalculateInstallmentCommandImpl();

  @Test
  void shouldCreateCreditSuccessfully() {
    //given
    String vehicleType = "mobil";
    String condition = "baru";
    int year = 2025;
    double totalLoan = 100_000_000;
    int tenure = 5;
    double dp = 35_000_000;
    CalculateInstallmentCommandRequest commandRequest =
        new CalculateInstallmentCommandRequest(vehicleType, condition, year, totalLoan, tenure, dp);
    //when
    CalculateInstallmentCommandResponse response = command.execute(commandRequest);
    //then
    assertNotNull(response);
    assertFalse(response.installments().isEmpty());
    List<CalculateInstallmentCommandResponse.Installment> installments = response.installments();
    assertEquals(tenure, installments.size());  }
}
